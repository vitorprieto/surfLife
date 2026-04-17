package com.br.originalTruta.surfLife.surf.controller;

import com.br.originalTruta.surfLife.common.response.ApiResponse;
import com.br.originalTruta.surfLife.surf.record.ExternalForecastBatchImportResponse;
import com.br.originalTruta.surfLife.surf.record.ExternalForecastImportResponse;
import com.br.originalTruta.surfLife.surf.service.ExternalForecastSyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/external-forecast")
@Tag(name = "External Forecast", description = "Imports forecast data from external provider")
public class ExternalForecastController {

    private final ExternalForecastSyncService externalForecastSyncService;

    public ExternalForecastController(ExternalForecastSyncService externalForecastSyncService) {
        this.externalForecastSyncService = externalForecastSyncService;
    }

    @PostMapping("/spots/{spotId}/import-latest")
    @Operation(
            summary = "Import latest external forecast",
            description = "Imports the latest forecast snapshot for a spot from Open-Meteo"
    )
    public ApiResponse<ExternalForecastImportResponse> importLatest(@PathVariable Long spotId) {
        return ApiResponse.success(
                "External forecast imported successfully.",
                externalForecastSyncService.importLatestForSpot(spotId)
        );
    }

    @PostMapping("/spots/{spotId}/import-multiple")
    @Operation(
            summary = "Import multiple external snapshots",
            description = "Imports multiple hourly forecast snapshots for a spot from Open-Meteo"
    )
    public ApiResponse<ExternalForecastBatchImportResponse> importMultiple(
            @PathVariable Long spotId,
            @RequestParam(required = false) Integer limit
    ) {
        return ApiResponse.success(
                "External forecast snapshots imported successfully.",
                externalForecastSyncService.importMultipleForSpot(spotId, limit)
        );
    }
}