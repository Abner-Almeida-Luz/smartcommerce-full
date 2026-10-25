package com.smartcommerce.backend.venda;

public class ItemCarrinhoNaoEncontradoException extends RuntimeException {
    public ItemCarrinhoNaoEncontradoException(Long produtoId) {
        super("Item com produto " + produtoId + " não encontrado no carrinho");
    }
}