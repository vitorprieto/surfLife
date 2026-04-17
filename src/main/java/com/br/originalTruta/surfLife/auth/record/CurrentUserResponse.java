package com.br.originalTruta.surfLife.auth.record;

public record CurrentUserResponse(
        Long id,
        String fullName,
        String email,
        String role
) {
}