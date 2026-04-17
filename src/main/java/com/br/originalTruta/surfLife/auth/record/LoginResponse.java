package com.br.originalTruta.surfLife.auth.record;

public record LoginResponse(
        String token,
        String tokenType,
        Long userId,
        String fullName,
        String email,
        String role
) {
}