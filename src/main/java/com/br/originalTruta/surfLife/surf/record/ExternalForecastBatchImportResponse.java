package com.br.originalTruta.surfLife.surf.record;

import java.util.List;

public record ExternalForecastBatchImportResponse(
        Long spotId,
        String spotName,
        String provider,
        int importedCount,
        List<ExternalForecastImportResponse> snapshots
) {
}