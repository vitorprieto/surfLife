package com.br.originalTruta.surfLife.surf.integration.worldtides;

import com.br.originalTruta.surfLife.config.WorldTidesProperties;
import com.br.originalTruta.surfLife.surf.integration.worldtides.record.WorldTidesResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class WorldTidesClient {

    private final RestClient restClient;
    private final WorldTidesProperties properties;

    public WorldTidesClient(
            RestClient restClient,
            WorldTidesProperties properties
    ) {
        this.restClient = restClient;
        this.properties = properties;
    }

    public WorldTidesResponse getTides(double latitude, double longitude) {
        return restClient.get()
                .uri(properties.baseUrl()
                                + "?heights"
                                + "&extremes"
                                + "&lat={lat}"
                                + "&lon={lon}"
                                + "&key={key}"
                                + "&datum={datum}"
                                + "&days={days}"
                                + "&localtime={localtime}",
                        latitude,
                        longitude,
                        properties.apiKey(),
                        properties.datum(),
                        properties.days(),
                        properties.localtime())
                .retrieve()
                .body(WorldTidesResponse.class);
    }
}