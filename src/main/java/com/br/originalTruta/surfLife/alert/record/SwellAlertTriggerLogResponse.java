package com.br.originalTruta.surfLife.alert.record;

import java.time.OffsetDateTime;

public record SwellAlertTriggerLogResponse(
        Long id,
        Long alertId,
        String alertName,
        Long userId,
        Long spotId,
        String spotName,
        Long forecastSnapshotId,
        OffsetDateTime triggeredAt,
        String reason,
        OffsetDateTime createdAt
) {
}