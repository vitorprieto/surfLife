package com.br.originalTruta.surfLife.surf.integration.openmeteo;

import com.br.originalTruta.surfLife.surf.integration.openmeteo.record.OpenMeteoMarineHourlyResponse;
import com.br.originalTruta.surfLife.surf.integration.openmeteo.record.OpenMeteoWeatherHourlyResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class OpenMeteoForecastClient {

    private final RestClient restClient;
    private final String marineBaseUrl;
    private final String timezone;
    private final String cellSelection;

    public OpenMeteoForecastClient(
            RestClient restClient,
            @Value("${app.forecast.open-meteo.base-url}") String marineBaseUrl,
            @Value("${app.forecast.open-meteo.timezone}") String timezone,
            @Value("${app.forecast.open-meteo.cell-selection}") String cellSelection
    ) {
        this.restClient = restClient;
        this.marineBaseUrl = marineBaseUrl;
        this.timezone = timezone;
        this.cellSelection = cellSelection;
    }

    public OpenMeteoMarineHourlyResponse getMarineForecast(double latitude, double longitude) {
        return restClient.get()
                .uri(
                        marineBaseUrl
                                + "/v1/marine"
                                + "?latitude={lat}"
                                + "&longitude={lon}"
                                + "&hourly=wave_height,wave_period,wave_direction,wind_wave_direction,sea_level_height_msl"
                                + "&timezone={timezone}"
                                + "&cell_selection={cellSelection}",
                        latitude,
                        longitude,
                        timezone,
                        cellSelection
                )
                .retrieve()
                .body(OpenMeteoMarineHourlyResponse.class);
    }

    public OpenMeteoWeatherHourlyResponse getWeatherForecast(double latitude, double longitude) {
        return restClient.get()
                .uri(
                        "https://api.open-meteo.com/v1/forecast"
                                + "?latitude={lat}"
                                + "&longitude={lon}"
                                + "&hourly=wind_speed_10m,wind_direction_10m"
                                + "&timezone={timezone}",
                        latitude,
                        longitude,
                        timezone
                )
                .retrieve()
                .body(OpenMeteoWeatherHourlyResponse.class);
    }
}