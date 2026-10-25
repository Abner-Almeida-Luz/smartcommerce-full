package com.smartcommerce.backend.venda;

import java.math.BigDecimal;

public record CarrinhoItemResponse(
        Long produtoId,
        String nomeProduto,
        Integer quantidade,
        BigDecimal precoUnitario,
        BigDecimal subtotal
) {
    public static CarrinhoItemResponse from(CarrinhoItem i) {
        return new CarrinhoItemResponse(
                i.getProduto().getId(),
                i.getProduto().getNome(),
                i.getQuantidade(),
                i.getPrecoUnitario(),
                i.getPrecoUnitario().multiply(BigDecimal.valueOf(i.getQuantidade()))
        );
    }
}