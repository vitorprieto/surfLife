package com.br.originalTruta.surfLife.alert.record;

import java.time.OffsetDateTime;

public record SwellAlertEvaluationResponse(
        Long alertId,
        String alertName,
        Long userId,
        Long spotId,
        String spotName,
        boolean triggered,
        String reason,
        Double currentWaveHeight,
        Integer currentWavePeriodSeconds,
        Double currentWindSpeed,
        String currentWindDirection,
        String currentTideState,
        OffsetDateTime observedAt
) {
}