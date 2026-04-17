package com.br.originalTruta.surfLife.alert.record;

import jakarta.validation.constraints.*;

public record CreateSwellAlertRequest(

        @NotBlank(message = "Name is required.")
        @Size(max = 120, message = "Name must have at most 120 characters.")
        String name,

        @NotNull(message = "Spot id is required.")
        Long spotId,

        @NotNull(message = "Minimum wave height is required.")
        @DecimalMin(value = "0.0", message = "Minimum wave height must be greater than or equal to 0.")
        Double minWaveHeight,

        @DecimalMin(value = "0.0", message = "Maximum wave height must be greater than or equal to 0.")
        Double maxWaveHeight,

        @NotNull(message = "Minimum period in seconds is required.")
        @Min(value = 1, message = "Minimum period must be at least 1 second.")
        Integer minPeriodSeconds,

        @DecimalMin(value = "0.0", message = "Maximum wind speed must be greater than or equal to 0.")
        Double maxWindSpeed,

        @Size(max = 200, message = "Preferred tides must have at most 200 characters.")
        String preferredTides,

        @Size(max = 200, message = "Preferred wind directions must have at most 200 characters.")
        String preferredWindDirections,

        Boolean enabled
) {
}