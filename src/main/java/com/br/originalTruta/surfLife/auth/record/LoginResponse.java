package com.br.originalTruta.surfLife.auth.record;

import com.br.originalTruta.surfLife.user.entity.UserRole;

public record LoginResponse(
        String token,
        String tokenType,
        Long userId,
        String fullName,
        String email,
        UserRole role
) {
}