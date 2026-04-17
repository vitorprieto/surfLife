package com.br.originalTruta.surfLife.alert.controller;

import com.br.originalTruta.surfLife.alert.record.SwellAlertEvaluationSummaryResponse;
import com.br.originalTruta.surfLife.alert.service.SwellAlertEvaluationService;
import com.br.originalTruta.surfLife.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/swell-alert-evaluations")
@Tag(name = "Swell Alert Evaluation", description = "Evaluates enabled swell alerts against the latest forecast snapshot")
public class SwellAlertEvaluationController {

    private final SwellAlertEvaluationService swellAlertEvaluationService;

    public SwellAlertEvaluationController(SwellAlertEvaluationService swellAlertEvaluationService) {
        this.swellAlertEvaluationService = swellAlertEvaluationService;
    }

    @GetMapping("/spot/{spotId}")
    @Operation(
            summary = "Evaluate alerts by spot",
            description = "Evaluates all enabled swell alerts for a spot using the latest forecast snapshot"
    )
    public ApiResponse<SwellAlertEvaluationSummaryResponse> evaluateBySpot(@PathVariable Long spotId) {
        return ApiResponse.success(
                "Swell alerts evaluated successfully.",
                swellAlertEvaluationService.evaluateBySpot(spotId)
        );
    }
}