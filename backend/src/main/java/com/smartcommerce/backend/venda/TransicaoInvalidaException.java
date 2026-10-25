package com.smartcommerce.backend.venda;

public class TransicaoInvalidaException extends RuntimeException {
    public TransicaoInvalidaException(StatusPedido atual, StatusPedido tentado) {
        super("Pedido em " + atual + " não pode ir para " + tentado);
    }
}