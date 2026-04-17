package com.br.originalTruta.surfLife.surf.controller;

import com.br.originalTruta.surfLife.common.response.ApiResponse;
import com.br.originalTruta.surfLife.surf.record.CreateForecastSnapshotRequest;
import com.br.originalTruta.surfLife.surf.record.ForecastSnapshotResponse;
import com.br.originalTruta.surfLife.surf.service.ForecastSnapshotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/forecast-snapshots")
@Tag(name = "Forecast Snapshots", description = "Manual forecast snapshots for surf spots")
public class ForecastSnapshotController {

    private final ForecastSnapshotService forecastSnapshotService;

    public ForecastSnapshotController(ForecastSnapshotService forecastSnapshotService) {
        this.forecastSnapshotService = forecastSnapshotService;
    }

    @PostMapping
    @Operation(summary = "Create forecast snapshot", description = "Creates a manual forecast snapshot for a spot")
    public ApiResponse<ForecastSnapshotResponse> create(@Valid @RequestBody CreateForecastSnapshotRequest request) {
        return ApiResponse.success(
                "Forecast snapshot created successfully.",
                forecastSnapshotService.create(request)
        );
    }
}