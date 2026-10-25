package com.smartcommerce.backend.cliente;

import jakarta.validation.constraints.*;

public record ClienteRequest(
        @NotBlank(message = "nome é obrigatório")
        @Size(max = 100)
        String nome,

        @NotBlank(message = "email é obrigatório")
        @Email(message = "email inválido")
        @Size(max = 150)
        String email,

        @NotBlank(message = "senha é obrigatória")
        @Size(min = 8, max = 72, message = "senha deve ter entre 8 e 72 caracteres")
        String senha
) {
}