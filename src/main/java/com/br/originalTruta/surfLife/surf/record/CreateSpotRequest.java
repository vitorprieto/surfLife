package com.br.originalTruta.surfLife.surf.record;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateSpotRequest(

        @NotBlank(message = "Name is required.")
        @Size(max = 120, message = "Name must have at most 120 characters.")
        String name,

        @NotBlank(message = "City is required.")
        @Size(max = 120, message = "City must have at most 120 characters.")
        String city,

        @NotBlank(message = "State is required.")
        @Size(max = 120, message = "State must have at most 120 characters.")
        String state,

        @NotBlank(message = "Country is required.")
        @Size(max = 120, message = "Country must have at most 120 characters.")
        String country,

        @NotNull(message = "Latitude is required.")
        @DecimalMin(value = "-90.0", message = "Latitude must be >= -90.")
        @DecimalMax(value = "90.0", message = "Latitude must be <= 90.")
        Double latitude,

        @NotNull(message = "Longitude is required.")
        @DecimalMin(value = "-180.0", message = "Longitude must be >= -180.")
        @DecimalMax(value = "180.0", message = "Longitude must be <= 180.")
        Double longitude
) {
}