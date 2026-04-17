package com.br.originalTruta.surfLife.user.record;

import com.br.originalTruta.surfLife.user.entity.UserRole;

import java.time.Instant;

public record UserResponse(
        Long id,
        String fullName,
        String email,
        UserRole role,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {
}