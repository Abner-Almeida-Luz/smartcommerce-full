package com.smartcommerce.backend.venda;

import com.smartcommerce.backend.cliente.Cliente;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carrinho")
public class Carrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false, unique = true)
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusCarrinho status = StatusCarrinho.ABERTO;

    @OneToMany(mappedBy = "carrinho", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarrinhoItem> itens = new ArrayList<>();

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    protected Carrinho() {}

    public Carrinho(Cliente cliente) {
        this.cliente = cliente;
    }

    @PrePersist
    void onCreate() {
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = this.criadoEm;
    }

    @PreUpdate
    void onUpdate() { this.atualizadoEm = LocalDateTime.now(); }

    public void adicionarItem(CarrinhoItem item) {
        itens.add(item);
        item.setCarrinho(this);
    }

    public void removerItem(CarrinhoItem item) {
        itens.remove(item);
        item.setCarrinho(null);
    }

    public void limpar() { itens.clear(); }

    // Getters
    public Long getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public StatusCarrinho getStatus() { return status; }
    public List<CarrinhoItem> getItens() { return itens; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
}