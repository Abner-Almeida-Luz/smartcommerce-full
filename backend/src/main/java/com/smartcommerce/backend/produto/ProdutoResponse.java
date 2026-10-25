package com.smartcommerce.backend.produto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProdutoResponse(
        Long id,
        String nome,
        BigDecimal preco,
        Integer estoque,
        Boolean ativo,
        LocalDateTime criadoEm
) {
    // 🔧 DECISÃO: método estático de conversão vive no DTO (não no Service)
    public static ProdutoResponse from(Produto p) {
        return new ProdutoResponse(
                p.getId(),
                p.getNome(),
                p.getPreco(),
                p.getEstoque(),
                p.getAtivo(),
                p.getCriadoEm()
        );
    }
}