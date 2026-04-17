package com.br.originalTruta.surfLife.surf.controller;

import com.br.originalTruta.surfLife.common.response.ApiResponse;
import com.br.originalTruta.surfLife.surf.record.SurfProfileResponse;
import com.br.originalTruta.surfLife.surf.record.UpsertSurfProfileRequest;
import com.br.originalTruta.surfLife.surf.service.SurfProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/surf-profile")
@Tag(name = "Surf Profile", description = "Authenticated user's surf profile")
public class SurfProfileController {

    private final SurfProfileService surfProfileService;

    public SurfProfileController(SurfProfileService surfProfileService) {
        this.surfProfileService = surfProfileService;
    }

    @GetMapping
    @Operation(summary = "Get my surf profile", description = "Returns the authenticated user's surf profile")
    public ApiResponse<SurfProfileResponse> getMine() {
        return ApiResponse.success(
                "Surf profile loaded successfully.",
                surfProfileService.getMine()
        );
    }

    @PutMapping
    @Operation(summary = "Create or update my surf profile", description = "Creates or updates the authenticated user's surf profile")
    public ApiResponse<SurfProfileResponse> upsertMine(@Valid @RequestBody UpsertSurfProfileRequest request) {
        return ApiResponse.success(
                "Surf profile saved successfully.",
                surfProfileService.upsertMine(request)
        );
    }
}