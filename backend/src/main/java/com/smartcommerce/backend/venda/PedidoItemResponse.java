package com.smartcommerce.backend.venda;

import java.math.BigDecimal;

public record PedidoItemResponse(
        Long produtoId,
        String nomeProduto,
        Integer quantidade,
        BigDecimal precoUnitario,
        BigDecimal subtotal
) {
    public static PedidoItemResponse from(PedidoItem i) {
        return new PedidoItemResponse(
                i.getProdutoId(),
                i.getNomeProduto(),
                i.getQuantidade(),
                i.getPrecoUnitario(),
                i.subtotal()
        );
    }
}