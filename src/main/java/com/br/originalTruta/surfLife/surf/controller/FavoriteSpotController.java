package com.br.originalTruta.surfLife.surf.controller;

import com.br.originalTruta.surfLife.common.response.ApiResponse;
import com.br.originalTruta.surfLife.surf.record.FavoriteSpotRequest;
import com.br.originalTruta.surfLife.surf.record.FavoriteSpotResponse;
import com.br.originalTruta.surfLife.surf.service.FavoriteSpotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorite-spots")
@Tag(name = "Favorite Spots", description = "Authenticated user's favorite spots")
public class FavoriteSpotController {

    private final FavoriteSpotService favoriteSpotService;

    public FavoriteSpotController(FavoriteSpotService favoriteSpotService) {
        this.favoriteSpotService = favoriteSpotService;
    }

    @PostMapping
    @Operation(summary = "Add favorite spot", description = "Adds a spot to the authenticated user's favorites")
    public ApiResponse<FavoriteSpotResponse> addMine(@Valid @RequestBody FavoriteSpotRequest request) {
        return ApiResponse.success(
                "Favorite spot added successfully.",
                favoriteSpotService.addMine(request)
        );
    }

    @GetMapping
    @Operation(summary = "List my favorite spots", description = "Returns the authenticated user's favorite spots")
    public ApiResponse<List<FavoriteSpotResponse>> listMine() {
        return ApiResponse.success(
                "Favorite spots loaded successfully.",
                favoriteSpotService.listMine()
        );
    }

    @DeleteMapping("/{spotId}")
    @Operation(summary = "Remove favorite spot", description = "Removes a spot from the authenticated user's favorites")
    public ApiResponse<Map<String, Object>> removeMine(@PathVariable Long spotId) {
        favoriteSpotService.removeMine(spotId);

        return ApiResponse.success(
                "Favorite spot removed successfully.",
                Map.of("spotId", spotId, "removed", true)
        );
    }
}