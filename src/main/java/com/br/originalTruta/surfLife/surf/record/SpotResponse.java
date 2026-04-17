package com.br.originalTruta.surfLife.surf.record;

import java.time.Instant;

public record SpotResponse(
        Long id,
        String name,
        String city,
        String state,
        String country,
        Double latitude,
        Double longitude,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {
}