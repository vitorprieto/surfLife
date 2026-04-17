package com.br.originalTruta.surfLife.alert.controller;

import com.br.originalTruta.surfLife.alert.record.CreateSwellAlertRequest;
import com.br.originalTruta.surfLife.alert.record.SwellAlertResponse;
import com.br.originalTruta.surfLife.alert.record.UpdateSwellAlertRequest;
import com.br.originalTruta.surfLife.alert.service.SwellAlertService;
import com.br.originalTruta.surfLife.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/swell-alerts")
@Tag(name = "Swell Alerts", description = "Authenticated user's swell alerts")
public class SwellAlertController {

    private final SwellAlertService swellAlertService;

    public SwellAlertController(SwellAlertService swellAlertService) {
        this.swellAlertService = swellAlertService;
    }

    @PostMapping
    @Operation(summary = "Create swell alert", description = "Creates a new swell alert for the authenticated user")
    public ApiResponse<SwellAlertResponse> createMine(@Valid @RequestBody CreateSwellAlertRequest request) {
        return ApiResponse.success(
                "Swell alert created successfully.",
                swellAlertService.createMine(request)
        );
    }

    @GetMapping
    @Operation(summary = "List my swell alerts", description = "Returns the authenticated user's swell alerts")
    public ApiResponse<List<SwellAlertResponse>> listMine() {
        return ApiResponse.success(
                "Swell alerts loaded successfully.",
                swellAlertService.listMine()
        );
    }

    @PutMapping("/{alertId}")
    @Operation(summary = "Update swell alert", description = "Updates a swell alert for the authenticated user")
    public ApiResponse<SwellAlertResponse> updateMine(
            @PathVariable Long alertId,
            @Valid @RequestBody UpdateSwellAlertRequest request
    ) {
        return ApiResponse.success(
                "Swell alert updated successfully.",
                swellAlertService.updateMine(alertId, request)
        );
    }

    @DeleteMapping("/{alertId}")
    @Operation(summary = "Delete swell alert", description = "Deletes a swell alert for the authenticated user")
    public ApiResponse<Map<String, Object>> deleteMine(@PathVariable Long alertId) {
        swellAlertService.deleteMine(alertId);

        return ApiResponse.success(
                "Swell alert deleted successfully.",
                Map.of("alertId", alertId, "deleted", true)
        );
    }
}