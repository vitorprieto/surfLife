package com.br.originalTruta.surfLife.surf.integration.openmeteo.record;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record OpenMeteoWeatherHourlyResponse(
        double latitude,
        double longitude,
        String timezone,
        Hourly hourly
) {

    public record Hourly(
            List<String> time,

            @JsonProperty("wind_speed_10m")
            List<Double> windSpeed10m,

            @JsonProperty("wind_direction_10m")
            List<Double> windDirection10m
    ) {
    }
}