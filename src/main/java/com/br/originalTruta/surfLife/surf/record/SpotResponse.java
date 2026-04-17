package com.br.originalTruta.surfLife.surf.record;

import java.time.OffsetDateTime;

public record SpotResponse(
        Long id,
        String name,
        String city,
        String state,
        String country,
        Double latitude,
        Double longitude,
        boolean active,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}