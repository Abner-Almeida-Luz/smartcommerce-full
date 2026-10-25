package com.smartcommerce.backend.venda;

import com.smartcommerce.backend.cliente.Cliente;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusPedido status = StatusPedido.AGUARDANDO_PAGAMENTO;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal frete;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "endereco_entrega", nullable = false)
    private String enderecoEntrega;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoItem> itens = new ArrayList<>();

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    protected Pedido() {}

    public Pedido(Cliente cliente, String enderecoEntrega) {
        this.cliente = cliente;
        this.enderecoEntrega = enderecoEntrega;
    }

    @PrePersist
    void onCreate() {
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = this.criadoEm;
    }

    @PreUpdate
    void onUpdate() { this.atualizadoEm = LocalDateTime.now(); }

    public void adicionarItem(PedidoItem item) {
        itens.add(item);
        item.setPedido(this);
    }

    public void fecharValores(BigDecimal subtotal, BigDecimal frete) {
        this.subtotal = subtotal;
        this.frete = frete;
        this.total = subtotal.add(frete);
    }

    public boolean podeIrPara(StatusPedido novo) {
        return switch (this.status) {
            case AGUARDANDO_PAGAMENTO -> novo == StatusPedido.PAGO || novo == StatusPedido.CANCELADO;
            case PAGO -> novo == StatusPedido.EM_SEPARACAO || novo == StatusPedido.CANCELADO;
            case EM_SEPARACAO -> novo == StatusPedido.ENVIADO;
            case ENVIADO -> novo == StatusPedido.ENTREGUE;
            case ENTREGUE, CANCELADO -> false;
        };
    }

    public void mudarStatus(StatusPedido novo) {
        this.status = novo;
    }

    public Long getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public StatusPedido getStatus() { return status; }
    public BigDecimal getSubtotal() { return subtotal; }
    public BigDecimal getFrete() { return frete; }
    public BigDecimal getTotal() { return total; }
    public String getEnderecoEntrega() { return enderecoEntrega; }
    public List<PedidoItem> getItens() { return itens; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
}