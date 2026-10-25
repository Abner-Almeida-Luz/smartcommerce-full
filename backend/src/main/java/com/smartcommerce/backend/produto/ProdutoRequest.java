package com.smartcommerce.backend.produto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProdutoRequest(
        @NotBlank(message = "nome é obrigatório")
        @Size(max = 100, message = "nome deve ter até 100 caracteres")
        String nome,

        @NotNull(message = "preço é obrigatório")
        @Positive(message = "preço deve ser positivo")
        BigDecimal preco,

        @NotNull(message = "estoque é obrigatório")
        @PositiveOrZero(message = "estoque não pode ser negativo")
        Integer estoque
) {
}