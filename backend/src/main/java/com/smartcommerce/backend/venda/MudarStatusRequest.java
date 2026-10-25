package com.smartcommerce.backend.venda;

import jakarta.validation.constraints.NotNull;

public record MudarStatusRequest(
        @NotNull(message = "novoStatus é obrigatório")
        StatusPedido novoStatus
) {}