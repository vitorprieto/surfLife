package com.br.originalTruta.surfLife.alert.record;

import java.time.OffsetDateTime;
import java.util.List;

public record SwellAlertEvaluationSummaryResponse(
        Long spotId,
        String spotName,
        OffsetDateTime observedAt,
        int totalAlertsEvaluated,
        int totalTriggered,
        List<SwellAlertEvaluationResponse> alerts
) {
}