package com.br.originalTruta.surfLife.surf.record;

import java.time.Instant;
import java.time.OffsetDateTime;

public record ForecastSnapshotResponse(
        Long id,
        Long spotId,
        String spotName,
        Double waveHeight,
        Integer wavePeriodSeconds,
        String swellDirection,
        Double windSpeed,
        String windDirection,
        String tideState,
        Double tideHeight,
        OffsetDateTime observedAt,
        Instant createdAt,
        Instant updatedAt
) {
}