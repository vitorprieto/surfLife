package com.br.originalTruta.surfLife.surf.record;

import com.br.originalTruta.surfLife.surf.entity.SurfLevel;

import java.time.Instant;

public record SurfProfileResponse(
        Long id,
        Long userId,
        SurfLevel level,
        Double minWaveHeight,
        Double maxWaveHeight,
        Integer minPeriodSeconds,
        String preferredTides,
        String offshoreDirections,
        Instant createdAt,
        Instant updatedAt
) {
}