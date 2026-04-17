package com.br.originalTruta.surfLife.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.tide.worldtides")
public record WorldTidesProperties(
        String baseUrl,
        String apiKey,
        String datum,
        Integer days,
        Boolean localtime
) {
}