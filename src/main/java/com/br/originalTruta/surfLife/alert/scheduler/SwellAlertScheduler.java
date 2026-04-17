package com.br.originalTruta.surfLife.alert.scheduler;

import com.br.originalTruta.surfLife.alert.service.SwellAlertEvaluationService;
import com.br.originalTruta.surfLife.alert.repository.SwellAlertRepository;
import com.br.originalTruta.surfLife.common.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SwellAlertScheduler {

    private static final Logger log = LoggerFactory.getLogger(SwellAlertScheduler.class);

    private final SwellAlertRepository swellAlertRepository;
    private final SwellAlertEvaluationService swellAlertEvaluationService;

    public SwellAlertScheduler(
            SwellAlertRepository swellAlertRepository,
            SwellAlertEvaluationService swellAlertEvaluationService
    ) {
        this.swellAlertRepository = swellAlertRepository;
        this.swellAlertEvaluationService = swellAlertEvaluationService;
    }

    @Scheduled(fixedDelayString = "${app.swell-alert.scheduler.fixed-delay-ms:300000}")
    public void evaluateEnabledAlerts() {
        List<Long> spotIds = swellAlertRepository.findDistinctEnabledSpotIds();

        if (spotIds.isEmpty()) {
            log.debug("Swell alert scheduler: no enabled alerts found.");
            return;
        }

        log.info("Swell alert scheduler started. Spots to evaluate: {}", spotIds.size());

        for (Long spotId : spotIds) {
            try {
                var summary = swellAlertEvaluationService.evaluateBySpot(spotId);

                log.info(
                        "Spot {} evaluated successfully. Alerts evaluated: {}, triggered: {}",
                        summary.spotName(),
                        summary.totalAlertsEvaluated(),
                        summary.totalTriggered()
                );
            } catch (ResourceNotFoundException ex) {
                log.warn(
                        "Skipping spot {} because latest forecast snapshot was not found. Reason: {}",
                        spotId,
                        ex.getMessage()
                );
            } catch (Exception ex) {
                log.error(
                        "Unexpected error while evaluating swell alerts for spot {}. Error: {}",
                        spotId,
                        ex.getMessage(),
                        ex
                );
            }
        }

        log.info("Swell alert scheduler finished.");
    }
}