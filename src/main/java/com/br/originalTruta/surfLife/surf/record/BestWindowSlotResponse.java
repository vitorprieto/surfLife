package com.br.originalTruta.surfLife.surf.record;

import com.br.originalTruta.surfLife.surf.entity.SurfDecisionLabel;

import java.time.OffsetDateTime;

public record BestWindowSlotResponse(
        OffsetDateTime observedAt,
        int score,
        SurfDecisionLabel label,
        String recommendation,
        Double waveHeight,
        Integer wavePeriodSeconds,
        Double windSpeed,
        String windDirection,
        String tideState
) {
}