package com.smartcommerce.backend.venda;

import java.math.BigDecimal;
import java.util.List;

public record CarrinhoResponse(
        Long id,
        String status,
        List<CarrinhoItemResponse> itens,
        BigDecimal total
) {
    public static CarrinhoResponse from(Carrinho c) {
        var itens = c.getItens().stream().map(CarrinhoItemResponse::from).toList();
        var total = itens.stream()
                .map(CarrinhoItemResponse::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new CarrinhoResponse(c.getId(), c.getStatus().name(), itens, total);
    }
}