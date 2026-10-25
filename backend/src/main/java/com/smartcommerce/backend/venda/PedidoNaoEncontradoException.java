package com.smartcommerce.backend.venda;

public class PedidoNaoEncontradoException extends RuntimeException {
    public PedidoNaoEncontradoException(Long id) {
        super("Pedido " + id + " não encontrado");
    }
}