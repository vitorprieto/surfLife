package com.br.originalTruta.surfLife.surf.record;

import com.br.originalTruta.surfLife.surf.entity.SurfLevel;
import jakarta.validation.constraints.*;

public record UpsertSurfProfileRequest(

        @NotNull(message = "Level is required.")
        SurfLevel level,

        @NotNull(message = "Minimum wave height is required.")
        @DecimalMin(value = "0.1", message = "Minimum wave height must be greater than 0.")
        Double minWaveHeight,

        @NotNull(message = "Maximum wave height is required.")
        @DecimalMin(value = "0.1", message = "Maximum wave height must be greater than 0.")
        Double maxWaveHeight,

        @NotNull(message = "Minimum period in seconds is required.")
        @Min(value = 1, message = "Minimum period must be at least 1 second.")
        Integer minPeriodSeconds,

        @Size(max = 200, message = "Preferred tides must have at most 200 characters.")
        String preferredTides,

        @Size(max = 200, message = "Offshore directions must have at most 200 characters.")
        String offshoreDirections
) {
}