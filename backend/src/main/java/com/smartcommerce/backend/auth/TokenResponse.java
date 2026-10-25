package com.smartcommerce.backend.auth;

public record TokenResponse(String token, String tipo, long expiraEmMinutos) {
    public static TokenResponse bearer(String token, int minutos) {
        return new TokenResponse(token, "Bearer", minutos);
    }
}