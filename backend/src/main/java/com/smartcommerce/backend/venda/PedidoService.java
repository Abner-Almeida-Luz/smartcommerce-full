package com.smartcommerce.backend.venda;

import com.smartcommerce.backend.cliente.ClienteRepository;
import com.smartcommerce.backend.produto.ProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class PedidoService {

    private static final BigDecimal FRETE_FIXO = new BigDecimal("15.00");
    private static final BigDecimal FRETE_GRATIS_ACIMA_DE = new BigDecimal("200.00");

    private final PedidoRepository pedidos;
    private final CarrinhoRepository carrinhos;
    private final ClienteRepository clientes;
    private final ProdutoRepository produtos;
    private final EstoqueMovimentoRepository movimentos;

    public PedidoService(PedidoRepository pedidos, CarrinhoRepository carrinhos,
                         ClienteRepository clientes, ProdutoRepository produtos,
                         EstoqueMovimentoRepository movimentos) {
        this.pedidos = pedidos;
        this.carrinhos = carrinhos;
        this.clientes = clientes;
        this.produtos = produtos;
        this.movimentos = movimentos;
    }

    @Transactional
    public PedidoResponse criar(String emailCliente, CheckoutRequest req) {
        var cliente = clientes.findByEmail(emailCliente)
                .orElseThrow(() -> new IllegalStateException("Cliente não existe: " + emailCliente));

        var carrinho = carrinhos.findByClienteId(cliente.getId())
                .orElseThrow(CarrinhoVazioException::new);

        if (carrinho.getItens().isEmpty()) {
            throw new CarrinhoVazioException();
        }

        var pedido = new Pedido(cliente, req.enderecoEntrega());

        // 1) revalida estoque e baixa
        for (var itemCarrinho : carrinho.getItens()) {
            var produto = produtos.findById(itemCarrinho.getProduto().getId())
                    .orElseThrow(() -> new ProdutoIndisponivelException(itemCarrinho.getProduto().getId(), "removido"));

            if (!produto.getAtivo()) {
                throw new ProdutoIndisponivelException(produto.getId(), "inativo");
            }
            if (produto.getEstoque() < itemCarrinho.getQuantidade()) {
                throw new ProdutoIndisponivelException(produto.getId(), "estoque insuficiente");
            }

            produto.baixarEstoque(itemCarrinho.getQuantidade());

            movimentos.save(new EstoqueMovimento(
                    produto.getId(),
                    TipoMovimentoEstoque.SAIDA,
                    itemCarrinho.getQuantidade(),
                    "Pedido de " + emailCliente
            ));
        }

        // 2) copia itens com snapshot para o pedido
        for (var itemCarrinho : carrinho.getItens()) {
            pedido.adicionarItem(new PedidoItem(
                    itemCarrinho.getProduto().getId(),
                    itemCarrinho.getProduto().getNome(),
                    itemCarrinho.getQuantidade(),
                    itemCarrinho.getPrecoUnitario()   // snapshot do momento em que entrou no carrinho
            ));
        }

        // 3) subtotal + frete + total
        var subtotal = pedido.getItens().stream()
                .map(PedidoItem::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        var frete = subtotal.compareTo(FRETE_GRATIS_ACIMA_DE) >= 0 ? BigDecimal.ZERO : FRETE_FIXO;
        pedido.fecharValores(subtotal, frete);

        // 4) fecha carrinho
        carrinho.limpar();
        carrinho.getItens().clear();   // garante limpeza
        carrinhos.save(carrinho);
        // TODO 1.4.c: mudar status para FECHADO quando tivermos máquina de estados do carrinho

        var salvo = pedidos.save(pedido);
        return PedidoResponse.from(salvo);
    }

    @Transactional(readOnly = true)
    public PedidoResponse buscar(String emailCliente, Long pedidoId) {
        var pedido = pedidos.findById(pedidoId)
                .orElseThrow(() -> new PedidoNaoEncontradoException(pedidoId));

        if (!pedido.getCliente().getEmail().equals(emailCliente)) {
            throw new PedidoNaoEncontradoException(pedidoId);   // 🔧 404, não 403 — não revela existência
        }
        return PedidoResponse.from(pedido);
    }

    @Transactional(readOnly = true)
    public Page<PedidoResponse> listarMeus(String emailCliente, Pageable pageable) {
        var cliente = clientes.findByEmail(emailCliente)
                .orElseThrow(() -> new IllegalStateException("Cliente não existe"));
        return pedidos.findByClienteId(cliente.getId(), pageable).map(PedidoResponse::from);
    }

    @Transactional
    public PedidoResponse mudarStatus(String emailCliente, boolean isAdmin, Long pedidoId, StatusPedido novo) {
        var pedido = pedidos.findById(pedidoId)
                .orElseThrow(() -> new PedidoNaoEncontradoException(pedidoId));

        boolean dono = pedido.getCliente().getEmail().equals(emailCliente);
        if (!dono && !isAdmin) {
            throw new PedidoNaoEncontradoException(pedidoId);   // 404 — não revela
        }

        // Regras de autorização por transição
        if (dono && !isAdmin) {
            // Cliente só pode: pagar o próprio pedido (AGUARDANDO_PAGAMENTO → PAGO)
            //                ou cancelar até PAGO
            if (novo == StatusPedido.PAGO && pedido.getStatus() != StatusPedido.AGUARDANDO_PAGAMENTO) {
                throw new TransicaoInvalidaException(pedido.getStatus(), novo);
            }
            if (novo != StatusPedido.PAGO && novo != StatusPedido.CANCELADO) {
                throw new TransicaoInvalidaException(pedido.getStatus(), novo);   // só admin faz EM_SEPARACAO, ENVIADO, ENTREGUE
            }
        }

        if (!pedido.podeIrPara(novo)) {
            throw new TransicaoInvalidaException(pedido.getStatus(), novo);
        }

        // Cancelamento: restaura estoque (só se já tinha baixado — ou seja, qualquer status não-CANCELADO)
        if (novo == StatusPedido.CANCELADO) {
            for (var item : pedido.getItens()) {
                var produto = produtos.findById(item.getProdutoId())
                        .orElseThrow(() -> new IllegalStateException("Produto sumiu: " + item.getProdutoId()));
                produto.aumentarEstoque(item.getQuantidade());

                movimentos.save(new EstoqueMovimento(
                        produto.getId(),
                        TipoMovimentoEstoque.ENTRADA,
                        item.getQuantidade(),
                        "Cancelamento pedido " + pedidoId
                ));
            }
        }

        pedido.mudarStatus(novo);
        return PedidoResponse.from(pedido);   // flush da transação persiste
    }

    private boolean isAdmin(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    @Transactional(readOnly = true)
    public Page<PedidoResponse> listarTodos(Pageable pageable) {
        return pedidos.findAllByOrderByCriadoEmDesc(pageable).map(PedidoResponse::from);
    }
}