package com.br.originalTruta.surfLife.surf.record;

import java.time.OffsetDateTime;

public record ExternalForecastImportResponse(
        Long spotId,
        String spotName,
        Long forecastSnapshotId,
        Double waveHeight,
        Integer wavePeriodSeconds,
        String swellDirection,
        Double windSpeed,
        String windDirection,
        String tideState,
        Double tideHeight,
        OffsetDateTime observedAt,
        String provider
) {
}