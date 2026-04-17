package com.br.originalTruta.surfLife.surf.controller;

import com.br.originalTruta.surfLife.common.response.ApiResponse;
import com.br.originalTruta.surfLife.surf.record.SurfDecisionResponse;
import com.br.originalTruta.surfLife.surf.service.SurfDecisionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/surf-decision")
@Tag(name = "Surf Decision", description = "Surf decision engine for authenticated user")
public class SurfDecisionController {

    private final SurfDecisionService surfDecisionService;

    public SurfDecisionController(SurfDecisionService surfDecisionService) {
        this.surfDecisionService = surfDecisionService;
    }

    @GetMapping("/{spotId}")
    @Operation(summary = "Get surf decision", description = "Returns surf decision for the authenticated user and selected spot")
    public ApiResponse<SurfDecisionResponse> decide(@PathVariable Long spotId) {
        return ApiResponse.success(
                "Surf decision calculated successfully.",
                surfDecisionService.decide(spotId)
        );
    }
}