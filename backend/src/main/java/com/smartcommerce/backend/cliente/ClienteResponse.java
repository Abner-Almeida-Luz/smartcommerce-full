package com.smartcommerce.backend.cliente;

import java.time.LocalDateTime;

public record ClienteResponse(
        Long id,
        String nome,
        String email,
        String role,
        LocalDateTime criadoEm
) {
    public static ClienteResponse from(Cliente c) {
        return new ClienteResponse(c.getId(), c.getNome(), c.getEmail(), c.getRole(), c.getCriadoEm());
    }
}