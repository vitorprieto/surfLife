package com.br.originalTruta.surfLife.surf.service;

import com.br.originalTruta.surfLife.common.exception.BusinessException;
import com.br.originalTruta.surfLife.surf.integration.worldtides.WorldTidesClient;
import com.br.originalTruta.surfLife.surf.integration.worldtides.record.WorldTidesResponse;
import com.br.originalTruta.surfLife.surf.record.TideInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class RealTideService {

    private final WorldTidesClient worldTidesClient;

    public RealTideService(WorldTidesClient worldTidesClient) {
        this.worldTidesClient = worldTidesClient;
    }

    @Transactional(readOnly = true)
    public TideInfo getTideInfo(double latitude, double longitude, OffsetDateTime observedAt) {
        WorldTidesResponse response = worldTidesClient.getTides(latitude, longitude);

        if (response == null || response.heights() == null || response.heights().isEmpty()) {
            throw new BusinessException("Tide provider returned no height data.");
        }

        List<WorldTidesResponse.HeightPoint> heights = response.heights();

        WorldTidesResponse.HeightPoint closest = heights.stream()
                .min(Comparator.comparingLong(point ->
                        Math.abs(point.dt() - observedAt.toEpochSecond())))
                .orElseThrow(() -> new BusinessException("Could not determine closest tide height."));

        String tideState = resolveTideState(heights, observedAt);

        return new TideInfo(
                closest.height(),
                tideState
        );
    }

    private String resolveTideState(List<WorldTidesResponse.HeightPoint> heights, OffsetDateTime observedAt) {
        long target = observedAt.toEpochSecond();

        WorldTidesResponse.HeightPoint previous = null;
        WorldTidesResponse.HeightPoint next = null;

        for (WorldTidesResponse.HeightPoint point : heights) {
            if (point.dt() <= target) {
                previous = point;
            }
            if (point.dt() > target) {
                next = point;
                break;
            }
        }

        if (previous == null || next == null) {
            return "MID";
        }

        double delta = next.height() - previous.height();

        if (Math.abs(delta) < 0.03) {
            return "MID";
        }

        return delta > 0 ? "RISING" : "FALLING";
    }
}