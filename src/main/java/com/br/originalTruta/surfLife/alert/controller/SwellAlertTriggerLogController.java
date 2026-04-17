package com.br.originalTruta.surfLife.alert.controller;

import com.br.originalTruta.surfLife.alert.record.SwellAlertTriggerLogResponse;
import com.br.originalTruta.surfLife.alert.service.SwellAlertTriggerLogService;
import com.br.originalTruta.surfLife.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/swell-alert-trigger-logs")
@Tag(name = "Swell Alert Trigger Logs", description = "Triggered swell alert history")
public class SwellAlertTriggerLogController {

    private final SwellAlertTriggerLogService swellAlertTriggerLogService;

    public SwellAlertTriggerLogController(SwellAlertTriggerLogService swellAlertTriggerLogService) {
        this.swellAlertTriggerLogService = swellAlertTriggerLogService;
    }

    @GetMapping
    @Operation(summary = "List my triggered alert logs", description = "Returns the authenticated user's triggered alert logs")
    public ApiResponse<List<SwellAlertTriggerLogResponse>> listMine() {
        return ApiResponse.success(
                "Triggered alert logs loaded successfully.",
                swellAlertTriggerLogService.listMine()
        );
    }

    @GetMapping("/alert/{alertId}")
    @Operation(summary = "List logs by alert", description = "Returns triggered logs for a specific alert")
    public ApiResponse<List<SwellAlertTriggerLogResponse>> listByAlert(@PathVariable Long alertId) {
        return ApiResponse.success(
                "Triggered alert logs loaded successfully.",
                swellAlertTriggerLogService.listByAlert(alertId)
        );
    }
}