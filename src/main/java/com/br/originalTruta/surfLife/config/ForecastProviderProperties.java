package com.br.originalTruta.surfLife.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.forecast.open-meteo")
public record ForecastProviderProperties(
        String baseUrl,
        String timezone,
        String cellSelection
) {
}