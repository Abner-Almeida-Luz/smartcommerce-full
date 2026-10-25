package com.smartcommerce.backend.venda;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CheckoutRequest(
        @NotBlank(message = "endereço de entrega é obrigatório")
        @Size(max = 255, message = "endereço muito longo")
        String enderecoEntrega
) {}