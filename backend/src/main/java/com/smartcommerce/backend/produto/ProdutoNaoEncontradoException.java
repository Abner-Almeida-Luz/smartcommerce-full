package com.smartcommerce.backend.produto;

public class ProdutoNaoEncontradoException extends RuntimeException {
    public ProdutoNaoEncontradoException(Long id) {
        super("Produto " + id + " não encontrado");
    }
}