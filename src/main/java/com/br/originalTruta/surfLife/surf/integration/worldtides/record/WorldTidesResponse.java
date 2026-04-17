package com.br.originalTruta.surfLife.surf.integration.worldtides.record;

import java.util.List;

public record WorldTidesResponse(
        String status,
        String timezone,
        String requestLat,
        String requestLon,
        List<HeightPoint> heights,
        List<ExtremePoint> extremes
) {

    public record HeightPoint(
            long dt,
            String date,
            Double height
    ) {
    }

    public record ExtremePoint(
            long dt,
            String date,
            Double height,
            String type
    ) {
    }
}