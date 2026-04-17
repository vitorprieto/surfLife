package com.br.originalTruta.surfLife.surf.service;

import com.br.originalTruta.surfLife.common.exception.BusinessException;
import com.br.originalTruta.surfLife.common.exception.ResourceNotFoundException;
import com.br.originalTruta.surfLife.surf.entity.ForecastSnapshot;
import com.br.originalTruta.surfLife.surf.entity.Spot;
import com.br.originalTruta.surfLife.surf.entity.SurfDecisionLabel;
import com.br.originalTruta.surfLife.surf.entity.SurfProfile;
import com.br.originalTruta.surfLife.surf.record.BestWindowResponse;
import com.br.originalTruta.surfLife.surf.record.BestWindowSlotResponse;
import com.br.originalTruta.surfLife.surf.repository.ForecastSnapshotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BestWindowService {

    private final ForecastSnapshotRepository forecastSnapshotRepository;
    private final SpotService spotService;
    private final SurfProfileQueryService surfProfileQueryService;

    public BestWindowService(
            ForecastSnapshotRepository forecastSnapshotRepository,
            SpotService spotService,
            SurfProfileQueryService surfProfileQueryService
    ) {
        this.forecastSnapshotRepository = forecastSnapshotRepository;
        this.spotService = spotService;
        this.surfProfileQueryService = surfProfileQueryService;
    }

    @Transactional(readOnly = true)
    public BestWindowResponse findBestWindow(Long spotId, OffsetDateTime from, OffsetDateTime to) {
        if (from == null || to == null) {
            throw new BusinessException("Both 'from' and 'to' are required.");
        }

        if (from.isAfter(to)) {
            throw new BusinessException("'from' cannot be after 'to'.");
        }

        Spot spot = spotService.findEntityById(spotId);
        SurfProfile profile = surfProfileQueryService.getMineEntity();

        List<ForecastSnapshot> snapshots = forecastSnapshotRepository
                .findBySpotIdAndObservedAtBetweenOrderByObservedAtAsc(spotId, from, to);

        if (snapshots.isEmpty()) {
            throw new ResourceNotFoundException("No forecast snapshots found in the requested time window.");
        }

        List<BestWindowSlotResponse> rankedSlots = snapshots.stream()
                .map(snapshot -> toSlot(snapshot, profile))
                .sorted(Comparator
                        .comparingInt(BestWindowSlotResponse::score).reversed()
                        .thenComparing(BestWindowSlotResponse::observedAt))
                .toList();

        BestWindowSlotResponse bestSlot = rankedSlots.get(0);

        return new BestWindowResponse(
                spot.getId(),
                spot.getName(),
                from,
                to,
                bestSlot,
                rankedSlots
        );
    }

    private BestWindowSlotResponse toSlot(ForecastSnapshot snapshot, SurfProfile profile) {
        int score = 0;

        score += calculateWaveHeightScore(snapshot, profile);
        score += calculateWavePeriodScore(snapshot, profile);
        score += calculateWindScore(snapshot, profile);
        score += calculateTideScore(snapshot, profile);

        SurfDecisionLabel label;
        String recommendation;

        if (score >= 85) {
            label = SurfDecisionLabel.GO_NOW;
            recommendation = "Excellent conditions for your profile.";
        } else if (score >= 65) {
            label = SurfDecisionLabel.WORTH_IT;
            recommendation = "Good conditions for your profile.";
        } else if (score >= 45) {
            label = SurfDecisionLabel.WAIT;
            recommendation = "Surfable, but not ideal yet.";
        } else {
            label = SurfDecisionLabel.DO_NOT_GO;
            recommendation = "Conditions are not favorable for your profile.";
        }

        return new BestWindowSlotResponse(
                snapshot.getObservedAt(),
                score,
                label,
                recommendation,
                snapshot.getWaveHeight(),
                snapshot.getWavePeriodSeconds(),
                snapshot.getWindSpeed(),
                snapshot.getWindDirection(),
                snapshot.getTideState()
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