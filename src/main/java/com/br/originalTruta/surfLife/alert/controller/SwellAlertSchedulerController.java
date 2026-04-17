package com.br.originalTruta.surfLife.alert.controller;

import com.br.originalTruta.surfLife.alert.scheduler.SwellAlertScheduler;
import com.br.originalTruta.surfLife.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/swell-alert-scheduler")
@Tag(name = "Swell Alert Scheduler", description = "Manual trigger for swell alert scheduler")
public class SwellAlertSchedulerController {

    private final SwellAlertScheduler swellAlertScheduler;

    public SwellAlertSchedulerController(SwellAlertScheduler swellAlertScheduler) {
        this.swellAlertScheduler = swellAlertScheduler;
    }

    @PostMapping("/run")
    @Operation(summary = "Run scheduler manually", description = "Triggers swell alert evaluation manually")
    public ApiResponse<Map<String, Object>> runNow() {
        swellAlertScheduler.evaluateEnabledAlerts();

        return ApiResponse.success(
                "Swell alert scheduler executed successfully.",
                Map.of("executed", true)
        );
    }
}