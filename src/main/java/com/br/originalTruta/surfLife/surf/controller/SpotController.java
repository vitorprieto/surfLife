package com.br.originalTruta.surfLife.surf.controller;

import com.br.originalTruta.surfLife.common.response.ApiResponse;
import com.br.originalTruta.surfLife.surf.record.CreateSpotRequest;
import com.br.originalTruta.surfLife.surf.record.SpotResponse;
import com.br.originalTruta.surfLife.surf.service.SpotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/spots")
@Tag(name = "Spots", description = "Surf spots management")
public class SpotController {

    private final SpotService spotService;

    public SpotController(SpotService spotService) {
        this.spotService = spotService;
    }

    @PostMapping
    @Operation(summary = "Create spot", description = "Creates a new surf spot")
    public ApiResponse<SpotResponse> create(@Valid @RequestBody CreateSpotRequest request) {
        return ApiResponse.success("Spot created successfully.", spotService.create(request));
    }

    @GetMapping
    @Operation(summary = "List active spots", description = "Returns all active surf spots")
    public ApiResponse<List<SpotResponse>> listActive() {
        return ApiResponse.success("Spots loaded successfully.", spotService.listActive());
    }
}