package com.br.originalTruta.surfLife.surf.record;

import java.time.OffsetDateTime;

public record FavoriteSpotResponse(
        Long favoriteId,
        Long spotId,
        String spotName,
        String city,
        String state,
        String country,
        Double latitude,
        Double longitude,
        OffsetDateTime createdAt
) {
}