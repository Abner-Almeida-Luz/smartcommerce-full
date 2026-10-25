package com.smartcommerce.backend.venda;

import com.smartcommerce.backend.cliente.Cliente;
import com.smartcommerce.backend.cliente.ClienteRepository;
import com.smartcommerce.backend.produto.Produto;
import com.smartcommerce.backend.produto.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CarrinhoService {

    private final CarrinhoRepository carrinhos;
    private final ClienteRepository clientes;
    private final ProdutoRepository produtos;

    public CarrinhoService(CarrinhoRepository carrinhos,
                           ClienteRepository clientes,
                           ProdutoRepository produtos) {
        this.carrinhos = carrinhos;
        this.clientes = clientes;
        this.produtos = produtos;
    }

    @Transactional
    public CarrinhoResponse obter(String emailCliente) {
        return CarrinhoResponse.from(buscarOuCriar(emailCliente));
    }

    @Transactional
    public CarrinhoResponse adicionar(String emailCliente, AdicionarItemRequest req) {
        var carrinho = buscarOuCriar(emailCliente);

        var produto = produtos.findById(req.produtoId())
                .filter(Produto::getAtivo)
                .orElseThrow(() -> new ProdutoIndisponivelException(req.produtoId(), "não existe ou inativo"));

        if (produto.getEstoque() < req.quantidade()) {
            throw new ProdutoIndisponivelException(req.produtoId(), "estoque insuficiente");
        }

        var itemExistente = carrinho.getItens().stream()
                .filter(i -> i.getProduto().getId().equals(produto.getId()))
                .findFirst();

        if (itemExistente.isPresent()) {
            itemExistente.get().incrementar(req.quantidade());
        } else {
            carrinho.adicionarItem(new CarrinhoItem(produto, req.quantidade()));
        }

        return CarrinhoResponse.from(carrinhos.save(carrinho));
    }

    @Transactional
    public CarrinhoResponse removerItem(String emailCliente, Long produtoId) {
        var carrinho = buscarOuCriar(emailCliente);

        var item = carrinho.getItens().stream()
                .filter(i -> i.getProduto().getId().equals(produtoId))
                .findFirst()
                .orElseThrow(() -> new ItemCarrinhoNaoEncontradoException(produtoId));

        carrinho.removerItem(item);
        return CarrinhoResponse.from(carrinhos.save(carrinho));
    }

    @Transactional
    public CarrinhoResponse limpar(String emailCliente) {
        var carrinho = buscarOuCriar(emailCliente);
        carrinho.limpar();
        return CarrinhoResponse.from(carrinhos.save(carrinho));
    }

    private Carrinho buscarOuCriar(String emailCliente) {
        var cliente = clientes.findByEmail(emailCliente)
                .orElseThrow(() -> new IllegalStateException("Cliente autenticado não existe: " + emailCliente));
        return carrinhos.findByClienteId(cliente.getId())
                .orElseGet(() -> carrinhos.save(new Carrinho(cliente)));
    }
}