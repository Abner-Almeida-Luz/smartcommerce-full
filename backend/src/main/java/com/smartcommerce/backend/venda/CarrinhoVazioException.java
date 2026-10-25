package com.smartcommerce.backend.venda;

public class CarrinhoVazioException extends RuntimeException {
    public CarrinhoVazioException() {
        super("Carrinho vazio — adicione itens antes de fechar o pedido");
    }
}