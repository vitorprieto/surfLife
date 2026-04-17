package com.br.originalTruta.surfLife.surf.service;

import com.br.originalTruta.surfLife.surf.entity.ForecastSnapshot;
import com.br.originalTruta.surfLife.surf.entity.SurfDecisionLabel;
import com.br.originalTruta.surfLife.surf.entity.SurfProfile;
import com.br.originalTruta.surfLife.surf.record.SurfDecisionResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SurfDecisionService {

    private final ForecastSnapshotService forecastSnapshotService;
    private final SurfProfileService surfProfileService;
    private final SurfProfileQueryService surfProfileQueryService;

    public SurfDecisionService(
            ForecastSnapshotService forecastSnapshotService,
            SurfProfileService surfProfileService,
            SurfProfileQueryService surfProfileQueryService
    ) {
        this.forecastSnapshotService = forecastSnapshotService;
        this.surfProfileService = surfProfileService;
        this.surfProfileQueryService = surfProfileQueryService;
    }

    @Transactional(readOnly = true)
    public SurfDecisionResponse decide(Long spotId) {
        ForecastSnapshot snapshot = forecastSnapshotService.findLatestEntityBySpotId(spotId);
        SurfProfile profile = surfProfileQueryService.getMineEntity();

        int score = 0;

        score += calculateWaveHeightScore(snapshot, profile);
        score += calculateWavePeriodScore(snapshot, profile);
        score += calculateWindScore(snapshot, profile);
        score += calculateTideScore(snapshot, profile);

        SurfDecisionLabel label;
        String recommendation;

        if (score >= 85) {
            label = SurfDecisionLabel.GO_NOW;
            recommendation = "Excellent conditions for your profile. Go now.";
        } else if (score >= 65) {
            label = SurfDecisionLabel.WORTH_IT;
            recommendation = "Good conditions for your profile. It is worth going.";
        } else if (score >= 45) {
            label = SurfDecisionLabel.WAIT;
            recommendation = "The conditions are surfable, but it may be better to wait.";
        } else {
            label = SurfDecisionLabel.DO_NOT_GO;
            recommendation = "Conditions are not favorable for your profile right now.";
        }

        String reason = buildReason(snapshot, profile);

        return new SurfDecisionResponse(
                snapshot.getSpot().getId(),
                snapshot.getSpot().getName(),
                score,
                label,
                recommendation,
                reason,
                snapshot.getWaveHeight(),
                snapshot.getWavePeriodSeconds(),
                snapshot.getWindSpeed(),
                snapshot.getWindDirection(),
                snapshot.getTideState(),
                snapshot.getObservedAt()
        );
    }

    private int calculateWaveHeightScore(ForecastSnapshot snapshot, SurfProfile profile) {
        double waveHeight = snapshot.getWaveHeight();
        double min = profile.getMinWaveHeight();
        double max = profile.getMaxWaveHeight();

        if (waveHeight < min) {
            return 0;
        }

        if (waveHeight > max) {
            return 10;
        }

        return 30;
    }

    private int calculateWavePeriodScore(ForecastSnapshot snapshot, SurfProfile profile) {
        int period = snapshot.getWavePeriodSeconds();
        int minPeriod = profile.getMinPeriodSeconds();

        if (period >= minPeriod + 3) {
            return 25;
        }

        if (period >= minPeriod) {
            return 18;
        }

        if (period >= Math.max(1, minPeriod - 2)) {
            return 8;
        }

        return 0;
    }

    private int calculateWindScore(ForecastSnapshot snapshot, SurfProfile profile) {
        Set<String> offshoreDirections = splitCsv(profile.getOffshoreDirections());
        String windDirection = normalize(snapshot.getWindDirection());
        double windSpeed = snapshot.getWindSpeed();

        boolean preferredOffshore = offshoreDirections.contains(windDirection);

        if (preferredOffshore && windSpeed <= 12) {
            return 25;
        }

        if (preferredOffshore && windSpeed <= 20) {
            return 18;
        }

        if (windSpeed <= 8) {
            return 8;
        }

        return 0;
    }

    private int calculateTideScore(ForecastSnapshot snapshot, SurfProfile profile) {
        Set<String> preferredTides = splitCsv(profile.getPreferredTides());
        String tideState = normalize(snapshot.getTideState());

        if (preferredTides.isEmpty()) {
            return 10;
        }

        return preferredTides.contains(tideState) ? 20 : 5;
    }

    private String buildReason(ForecastSnapshot snapshot, SurfProfile profile) {
        return "Wave height " + snapshot.getWaveHeight() + "m, period "
                + snapshot.getWavePeriodSeconds() + "s, wind "
                + snapshot.getWindDirection() + " at " + snapshot.getWindSpeed()
                + ", tide " + snapshot.getTideState()
                + " compared to your profile preferences.";
    }

    private Set<String> splitCsv(String value) {
        if (value == null || value.isBlank()) {
            return Set.of();
        }

        return Arrays.stream(value.split(","))
                .map(this::normalize)
                .filter(item -> !item.isBlank())
                .collect(Collectors.toSet());
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toUpperCase(Locale.ROOT);
    }
}