package com.br.originalTruta.surfLife.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ForecastProviderProperties.class)
public class ForecastProviderConfig {
}