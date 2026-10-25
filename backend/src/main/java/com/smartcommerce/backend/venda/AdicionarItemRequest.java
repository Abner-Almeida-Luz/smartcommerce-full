package com.smartcommerce.backend.venda;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AdicionarItemRequest(
        @NotNull(message = "produtoId é obrigatório")
        Long produtoId,

        @NotNull(message = "quantidade é obrigatória")
        @Positive(message = "quantidade deve ser positiva")
        Integer quantidade
) {}