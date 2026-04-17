package com.br.originalTruta.surfLife.surf.integration.openmeteo.record;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record OpenMeteoMarineHourlyResponse(
        double latitude,
        double longitude,
        String timezone,
        Hourly hourly
) {

    public record Hourly(
            List<String> time,

            @JsonProperty("wave_height")
            List<Double> waveHeight,

            @JsonProperty("wave_period")
            List<Double> wavePeriod,

            @JsonProperty("wave_direction")
            List<Double> waveDirection,

            @JsonProperty("wind_wave_direction")
            List<Double> windWaveDirection,

            @JsonProperty("sea_level_height_msl")
            List<Double> seaLevelHeight
    ) {
    }
}