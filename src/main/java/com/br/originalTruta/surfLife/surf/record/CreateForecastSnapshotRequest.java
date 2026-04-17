package com.br.originalTruta.surfLife.surf.record;

import jakarta.validation.constraints.*;

import java.time.OffsetDateTime;

public record CreateForecastSnapshotRequest(

        @NotNull(message = "Spot id is required.")
        Long spotId,

        @NotNull(message = "Wave height is required.")
        @DecimalMin(value = "0.0", message = "Wave height must be greater than or equal to 0.")
        Double waveHeight,

        @NotNull(message = "Wave period is required.")
        @Min(value = 1, message = "Wave period must be at least 1 second.")
        Integer wavePeriodSeconds,

        @NotBlank(message = "Swell direction is required.")
        @Size(max = 20, message = "Swell direction must have at most 20 characters.")
        String swellDirection,

        @NotNull(message = "Wind speed is required.")
        @DecimalMin(value = "0.0", message = "Wind speed must be greater than or equal to 0.")
        Double windSpeed,

        @NotBlank(message = "Wind direction is required.")
        @Size(max = 20, message = "Wind direction must have at most 20 characters.")
        String windDirection,

        @NotBlank(message = "Tide state is required.")
        @Size(max = 20, message = "Tide state must have at most 20 characters.")
        String tideState,

        @NotNull(message = "Tide height is required.")
        @DecimalMin(value = "0.0", message = "Tide height must be greater than or equal to 0.")
        Double tideHeight,

        @NotNull(message = "Observed at is required.")
        OffsetDateTime observedAt
) {
}