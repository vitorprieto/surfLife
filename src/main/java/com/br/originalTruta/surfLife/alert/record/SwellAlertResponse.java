package com.br.originalTruta.surfLife.alert.record;

import java.time.OffsetDateTime;

public record SwellAlertResponse(
        Long id,
        String name,
        Long userId,
        Long spotId,
        String spotName,
        Double minWaveHeight,
        Double maxWaveHeight,
        Integer minPeriodSeconds,
        Double maxWindSpeed,
        String preferredTides,
        String preferredWindDirections,
        boolean enabled,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}