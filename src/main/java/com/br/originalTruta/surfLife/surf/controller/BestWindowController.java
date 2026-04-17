package com.br.originalTruta.surfLife.surf.controller;

import com.br.originalTruta.surfLife.common.response.ApiResponse;
import com.br.originalTruta.surfLife.surf.record.BestWindowResponse;
import com.br.originalTruta.surfLife.surf.service.BestWindowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/best-window")
@Tag(name = "Best Window", description = "Best surf window for the authenticated user")
public class BestWindowController {

    private final BestWindowService bestWindowService;

    public BestWindowController(BestWindowService bestWindowService) {
        this.bestWindowService = bestWindowService;
    }

    @GetMapping("/{spotId}")
    @Operation(
            summary = "Get best window of the day",
            description = "Returns the best ranked surf window for the authenticated user and selected spot"
    )
    public ApiResponse<BestWindowResponse> findBestWindow(
            @PathVariable Long spotId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime to
    ) {
        return ApiResponse.success(
                "Best window calculated successfully.",
                bestWindowService.findBestWindow(spotId, from, to)
        );
    }
}