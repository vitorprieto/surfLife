package com.br.originalTruta.surfLife.alert.record;

import java.time.Instant;

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
        Instant createdAt,
        Instant updatedAt
) {
}