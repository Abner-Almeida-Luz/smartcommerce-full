package com.smartcommerce.backend.produto;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(nullable = false)
    private Integer estoque;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    // 🔧 DECISÃO: construtor vazio protegido para JPA + construtor de negócio
    protected Produto() {}

    public Produto(String nome, BigDecimal preco, Integer estoque) {
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
        this.ativo = true;
    }

    // 🔧 DECISÃO: @PrePersist garante criadoEm mesmo se esquecer de setar
    @PrePersist
    void onCreate() {
        this.criadoEm = LocalDateTime.now();
    }

    // 🔧 DECISÃO: atualizar via método (não setter direto) — encapsula regra
    public void atualizar(String nome, BigDecimal preco, Integer estoque) {
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
    }

    public void desativar() {
        this.ativo = false;
    }

    public void baixarEstoque(int qtd) {
        if (this.estoque < qtd) {
            throw new IllegalStateException("Estoque insuficiente para " + nome);
        }
        this.estoque -= qtd;
    }

    public void aumentarEstoque(int qtd) {
        this.estoque += qtd;
    }

    // Getters
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public BigDecimal getPreco() { return preco; }
    public Integer getEstoque() { return estoque; }
    public Boolean getAtivo() { return ativo; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
}