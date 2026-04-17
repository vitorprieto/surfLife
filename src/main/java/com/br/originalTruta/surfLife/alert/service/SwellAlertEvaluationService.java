package com.br.originalTruta.surfLife.alert.service;

import com.br.originalTruta.surfLife.alert.entity.SwellAlert;
import com.br.originalTruta.surfLife.alert.record.SwellAlertEvaluationResponse;
import com.br.originalTruta.surfLife.alert.record.SwellAlertEvaluationSummaryResponse;
import com.br.originalTruta.surfLife.alert.repository.SwellAlertRepository;
import com.br.originalTruta.surfLife.surf.entity.ForecastSnapshot;
import com.br.originalTruta.surfLife.surf.entity.Spot;
import com.br.originalTruta.surfLife.surf.service.ForecastSnapshotService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SwellAlertEvaluationService {

    private final SwellAlertRepository swellAlertRepository;
    private final ForecastSnapshotService forecastSnapshotService;
    private final SwellAlertTriggerLogService swellAlertTriggerLogService;

    public SwellAlertEvaluationService(
            SwellAlertRepository swellAlertRepository,
            ForecastSnapshotService forecastSnapshotService,
            SwellAlertTriggerLogService swellAlertTriggerLogService
    ) {
        this.swellAlertRepository = swellAlertRepository;
        this.forecastSnapshotService = forecastSnapshotService;
        this.swellAlertTriggerLogService = swellAlertTriggerLogService;
    }

    @Transactional
    public SwellAlertEvaluationSummaryResponse evaluateBySpot(Long spotId) {
        ForecastSnapshot snapshot = forecastSnapshotService.findLatestEntityBySpotId(spotId);
        List<SwellAlert> alerts = swellAlertRepository.findBySpotIdAndEnabledTrue(spotId);

        List<SwellAlertEvaluationResponse> evaluations = alerts.stream()
                .map(alert -> evaluateAlert(alert, snapshot))
                .toList();

        long totalTriggered = evaluations.stream()
                .filter(SwellAlertEvaluationResponse::triggered)
                .count();

        Spot spot = snapshot.getSpot();

        return new SwellAlertEvaluationSummaryResponse(
                spot.getId(),
                spot.getName(),
                snapshot.getObservedAt(),
                evaluations.size(),
                (int) totalTriggered,
                evaluations
        );
    }

    private SwellAlertEvaluationResponse evaluateAlert(SwellAlert alert, ForecastSnapshot snapshot) {
        String evaluationResult = evaluateReason(alert, snapshot);
        boolean triggered = "TRIGGERED".equals(evaluationResult);

        String reason;
        if (triggered) {
            reason = buildSuccessReason(alert, snapshot);
            swellAlertTriggerLogService.createIfNotExists(alert, snapshot, reason);
        } else {
            reason = evaluationResult;
        }

        return new SwellAlertEvaluationResponse(
                alert.getId(),
                alert.getName(),
                alert.getUser().getId(),
                snapshot.getSpot().getId(),
                snapshot.getSpot().getName(),
                triggered,
                reason,
                snapshot.getWaveHeight(),
                snapshot.getWavePeriodSeconds(),
                snapshot.getWindSpeed(),
                snapshot.getWindDirection(),
                snapshot.getTideState(),
                snapshot.getObservedAt()
        );
    }

    private String evaluateReason(SwellAlert alert, ForecastSnapshot snapshot) {
        if (snapshot.getWaveHeight() < alert.getMinWaveHeight()) {
            return "Wave height is below the configured minimum.";
        }

        if (alert.getMaxWaveHeight() != null && snapshot.getWaveHeight() > alert.getMaxWaveHeight()) {
            return "Wave height is above the configured maximum.";
        }

        if (snapshot.getWavePeriodSeconds() < alert.getMinPeriodSeconds()) {
            return "Wave period is below the configured minimum.";
        }

        if (alert.getMaxWindSpeed() != null && snapshot.getWindSpeed() > alert.getMaxWindSpeed()) {
            return "Wind speed is above the configured maximum.";
        }

        Set<String> preferredWindDirections = splitCsv(alert.getPreferredWindDirections());
        if (!preferredWindDirections.isEmpty()
                && !preferredWindDirections.contains(normalize(snapshot.getWindDirection()))) {
            return "Wind direction does not match the configured preferences.";
        }

        Set<String> preferredTides = splitCsv(alert.getPreferredTides());
        if (!preferredTides.isEmpty()
                && !preferredTides.contains(normalize(snapshot.getTideState()))) {
            return "Tide state does not match the configured preferences.";
        }

        return "TRIGGERED";
    }

    private String buildSuccessReason(SwellAlert alert, ForecastSnapshot snapshot) {
        return "Triggered: wave "
                + snapshot.getWaveHeight() + "m, period "
                + snapshot.getWavePeriodSeconds() + "s, wind "
                + snapshot.getWindDirection() + " at "
                + snapshot.getWindSpeed() + ", tide "
                + snapshot.getTideState()
                + " matched alert '" + alert.getName() + "'.";
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