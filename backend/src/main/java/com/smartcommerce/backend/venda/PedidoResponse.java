package com.smartcommerce.backend.venda;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse(
        Long id,
        String status,
        String enderecoEntrega,
        List<PedidoItemResponse> itens,
        BigDecimal subtotal,
        BigDecimal frete,
        BigDecimal total,
        LocalDateTime criadoEm
) {
    public static PedidoResponse from(Pedido p) {
        return new PedidoResponse(
                p.getId(),
                p.getStatus().name(),
                p.getEnderecoEntrega(),
                p.getItens().stream().map(PedidoItemResponse::from).toList(),
                p.getSubtotal(),
                p.getFrete(),
                p.getTotal(),
                p.getCriadoEm()
        );
    }
}