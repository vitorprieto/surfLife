package com.br.originalTruta.surfLife.auth.record;

import com.br.originalTruta.surfLife.user.entity.UserRole;

import java.time.OffsetDateTime;

public record RegisterResponse(
        Long id,
        String fullName,
        String email,
        UserRole role,
        boolean active,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}