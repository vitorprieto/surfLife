package com.br.originalTruta.surfLife.alert.scheduler;

import com.br.originalTruta.surfLife.alert.repository.SwellAlertRepository;
import com.br.originalTruta.surfLife.alert.service.SwellAlertEvaluationService;
import com.br.originalTruta.surfLife.common.exception.ResourceNotFoundException;
import com.br.originalTruta.surfLife.surf.entity.Spot;
import com.br.originalTruta.surfLife.surf.service.ExternalForecastSyncService;
import com.br.originalTruta.surfLife.surf.service.SpotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SwellAlertScheduler {

    private static final Logger log = LoggerFactory.getLogger(SwellAlertScheduler.class);

    private final SpotService spotService;
    private final ExternalForecastSyncService externalForecastSyncService;
    private final SwellAlertRepository swellAlertRepository;
    private final SwellAlertEvaluationService swellAlertEvaluationService;

    public SwellAlertScheduler(
            SpotService spotService,
            ExternalForecastSyncService externalForecastSyncService,
            SwellAlertRepository swellAlertRepository,
            SwellAlertEvaluationService swellAlertEvaluationService
    ) {
        this.spotService = spotService;
        this.externalForecastSyncService = externalForecastSyncService;
        this.swellAlertRepository = swellAlertRepository;
        this.swellAlertEvaluationService = swellAlertEvaluationService;
    }

    @Scheduled(
            fixedDelayString = "${app.swell-alert.scheduler.fixed-delay-ms:300000}",
            initialDelayString = "${app.swell-alert.scheduler.initial-delay-ms:30000}"
    )
    public void syncForecastAndEvaluateAlerts() {
        List<Spot> activeSpots = spotService.listActiveEntities();

        if (activeSpots.isEmpty()) {
            log.debug("Unified scheduler: no active spots found.");
            return;
        }

        log.info("Unified scheduler started. Active spots to process: {}", activeSpots.size());

        for (Spot spot : activeSpots) {
            try {
                int importedCount = externalForecastSyncService.importMultipleForSpotForScheduler(
                        spot.getId(),
                        6
                );

                log.info(
                        "Spot {} forecast imported successfully. Imported snapshots: {}",
                        spot.getName(),
                        importedCount
                );
            } catch (Exception ex) {
                log.error(
                        "Failed to import external forecast for spot {}. Error: {}",
                        spot.getName(),
                        ex.getMessage(),
                        ex
                );
                continue;
            }

            try {
                boolean hasEnabledAlerts = !swellAlertRepository.findBySpotIdAndEnabledTrue(spot.getId()).isEmpty();

                if (!hasEnabledAlerts) {
                    log.debug("Spot {} has no enabled alerts. Skipping alert evaluation.", spot.getName());
                    continue;
                }

                var summary = swellAlertEvaluationService.evaluateBySpot(spot.getId());

                log.info(
                        "Spot {} evaluated successfully. Alerts evaluated: {}, triggered: {}",
                        summary.spotName(),
                        summary.totalAlertsEvaluated(),
                        summary.totalTriggered()
                );
            } catch (ResourceNotFoundException ex) {
                log.warn(
                        "Skipping alert evaluation for spot {} because latest forecast snapshot was not found. Reason: {}",
                        spot.getName(),
                        ex.getMessage()
                );
            } catch (Exception ex) {
                log.error(
                        "Unexpected error while evaluating alerts for spot {}. Error: {}",
                        spot.getName(),
                        ex.getMessage(),
                        ex
                );
            }
        }

        log.info("Unified scheduler finished.");
    }
}