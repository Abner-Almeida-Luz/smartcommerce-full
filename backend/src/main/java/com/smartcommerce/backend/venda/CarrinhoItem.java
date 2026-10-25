package com.smartcommerce.backend.venda;

import com.smartcommerce.backend.produto.Produto;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "carrinho_item",
        uniqueConstraints = @UniqueConstraint(columnNames = {"carrinho_id", "produto_id"}))
public class CarrinhoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "carrinho_id", nullable = false)
    private Carrinho carrinho;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "preco_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario;

    protected CarrinhoItem() {}

    public CarrinhoItem(Produto produto, Integer quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = produto.getPreco();  // snapshot
    }

    public void incrementar(int qtd) {
        this.quantidade += qtd;
    }

    // Getters e setter só do carrinho (bidirecionalidade)
    public Long getId() { return id; }
    public Carrinho getCarrinho() { return carrinho; }
    void setCarrinho(Carrinho c) { this.carrinho = c; }
    public Produto getProduto() { return produto; }
    public Integer getQuantidade() { return quantidade; }
    public BigDecimal getPrecoUnitario() { return precoUnitario; }
}