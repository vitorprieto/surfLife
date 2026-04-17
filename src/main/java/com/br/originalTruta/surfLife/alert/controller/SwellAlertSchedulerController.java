package com.br.originalTruta.surfLife.alert.controller;

import com.br.originalTruta.surfLife.alert.scheduler.SwellAlertScheduler;
import com.br.originalTruta.surfLife.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/swell-alert-scheduler")
@Tag(name = "Swell Alert Scheduler", description = "Manual trigger for forecast sync and swell alert evaluation")
public class SwellAlertSchedulerController {

    private final SwellAlertScheduler swellAlertScheduler;

    public SwellAlertSchedulerController(SwellAlertScheduler swellAlertScheduler) {
        this.swellAlertScheduler = swellAlertScheduler;
    }

    @PostMapping("/run")
    @Operation(
            summary = "Run scheduler manually",
            description = "Triggers forecast import for active spots and evaluates enabled swell alerts"
    )
    public ApiResponse<Map<String, Object>> runNow() {
        swellAlertScheduler.syncForecastAndEvaluateAlerts();

        return ApiResponse.success(
                "Forecast sync and swell alert evaluation executed successfully.",
                Map.of("executed", true)
        );
    }
}