package com.br.originalTruta.surfLife.alert.service;

import com.br.originalTruta.surfLife.alert.entity.SwellAlert;
import com.br.originalTruta.surfLife.alert.entity.SwellAlertTriggerLog;
import com.br.originalTruta.surfLife.alert.record.SwellAlertTriggerLogResponse;
import com.br.originalTruta.surfLife.alert.repository.SwellAlertTriggerLogRepository;
import com.br.originalTruta.surfLife.auth.service.CurrentUserService;
import com.br.originalTruta.surfLife.security.AuthenticatedUser;
import com.br.originalTruta.surfLife.surf.entity.ForecastSnapshot;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class SwellAlertTriggerLogService {

    private final SwellAlertTriggerLogRepository swellAlertTriggerLogRepository;
    private final CurrentUserService currentUserService;

    public SwellAlertTriggerLogService(
            SwellAlertTriggerLogRepository swellAlertTriggerLogRepository,
            CurrentUserService currentUserService
    ) {
        this.swellAlertTriggerLogRepository = swellAlertTriggerLogRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public void createIfNotExists(SwellAlert alert, ForecastSnapshot snapshot, String reason) {
        boolean alreadyExists = swellAlertTriggerLogRepository.existsByAlertIdAndForecastSnapshotId(
                alert.getId(),
                snapshot.getId()
        );

        if (alreadyExists) {
            return;
        }

        SwellAlertTriggerLog log = new SwellAlertTriggerLog();
        log.setAlert(alert);
        log.setUser(alert.getUser());
        log.setSpot(alert.getSpot());
        log.setForecastSnapshot(snapshot);
        log.setTriggeredAt(OffsetDateTime.now());
        log.setReason(reason);

        swellAlertTriggerLogRepository.save(log);
    }

    @Transactional(readOnly = true)
    public List<SwellAlertTriggerLogResponse> listMine() {
        AuthenticatedUser currentUser = currentUserService.getAuthenticatedUser();

        return swellAlertTriggerLogRepository.findByUserIdOrderByTriggeredAtDesc(currentUser.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SwellAlertTriggerLogResponse> listByAlert(Long alertId) {
        return swellAlertTriggerLogRepository.findByAlertIdOrderByTriggeredAtDesc(alertId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private SwellAlertTriggerLogResponse toResponse(SwellAlertTriggerLog log) {
        return new SwellAlertTriggerLogResponse(
                log.getId(),
                log.getAlert().getId(),
                log.getAlert().getName(),
                log.getUser().getId(),
                log.getSpot().getId(),
                log.getSpot().getName(),
                log.getForecastSnapshot().getId(),
                log.getTriggeredAt(),
                log.getReason(),
                log.getCreatedAt()
        );
    }
}