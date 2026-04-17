package com.br.originalTruta.surfLife.surf.record;

import jakarta.validation.constraints.NotNull;

public record FavoriteSpotRequest(
        @NotNull(message = "Spot id is required.")
        Long spotId
) {
}