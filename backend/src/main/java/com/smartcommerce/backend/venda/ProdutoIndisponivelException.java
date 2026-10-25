package com.smartcommerce.backend.venda;

public class ProdutoIndisponivelException extends RuntimeException {
    public ProdutoIndisponivelException(Long produtoId, String motivo) {
        super("Produto " + produtoId + " indisponível: " + motivo);
    }
}