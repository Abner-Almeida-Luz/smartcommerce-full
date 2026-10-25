package com.smartcommerce.backend.venda;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "estoque_movimento")
public class EstoqueMovimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "produto_id", nullable = false)
    private Long produtoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoMovimentoEstoque tipo;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(length = 255)
    private String motivo;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    protected EstoqueMovimento() {}

    public EstoqueMovimento(Long produtoId, TipoMovimentoEstoque tipo, Integer quantidade, String motivo) {
        this.produtoId = produtoId;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.motivo = motivo;
    }

    @PrePersist
    void onCreate() { this.criadoEm = LocalDateTime.now(); }

    public Long getId() { return id; }
    public Long getProdutoId() { return produtoId; }
    public TipoMovimentoEstoque getTipo() { return tipo; }
    public Integer getQuantidade() { return quantidade; }
    public String getMotivo() { return motivo; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
}