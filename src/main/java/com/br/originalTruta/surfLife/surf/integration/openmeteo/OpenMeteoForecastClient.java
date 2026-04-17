package com.br.originalTruta.surfLife.surf.integration.openmeteo;

import com.br.originalTruta.surfLife.config.ForecastProviderProperties;
import com.br.originalTruta.surfLife.surf.integration.openmeteo.record.OpenMeteoMarineHourlyResponse;
import com.br.originalTruta.surfLife.surf.integration.openmeteo.record.OpenMeteoWeatherHourlyResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class OpenMeteoForecastClient {

    private final RestClient restClient;
    private final ForecastProviderProperties properties;

    public OpenMeteoForecastClient(
            RestClient restClient,
            ForecastProviderProperties properties
    ) {
        this.restClient = restClient;
        this.properties = properties;
    }

    public OpenMeteoMarineHourlyResponse getMarineForecast(double latitude, double longitude) {
        return restClient.get()
                .uri(properties.baseUrl() + "/v1/marine?latitude={lat}&longitude={lon}&hourly=wave_height,wave_period,wave_direction,wind_wave_direction&timezone={timezone}&cell_selection={cellSelection}",
                        latitude,
                        longitude,
                        properties.timezone(),
                        properties.cellSelection())
                .retrieve()
                .body(OpenMeteoMarineHourlyResponse.class);
    }

    public OpenMeteoWeatherHourlyResponse getWeatherForecast(double latitude, double longitude) {
        return restClient.get()
                .uri("https://api.open-meteo.com/v1/forecast?latitude={lat}&longitude={lon}&hourly=wind_speed_10m,wind_direction_10m&timezone={timezone}",
                        latitude,
                        longitude,
                        properties.timezone())
                .retrieve()
                .body(OpenMeteoWeatherHourlyResponse.class);
    }
}