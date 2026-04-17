package com.br.originalTruta.surfLife.surf.record;

import com.br.originalTruta.surfLife.surf.entity.SurfLevel;

import java.time.OffsetDateTime;

public record SurfProfileResponse(
        Long id,
        Long userId,
        SurfLevel level,
        Double minWaveHeight,
        Double maxWaveHeight,
        Integer minPeriodSeconds,
        String preferredTides,
        String offshoreDirections,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}