package com.br.originalTruta.surfLife.alert.repository;

import com.br.originalTruta.surfLife.alert.entity.SwellAlertTriggerLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SwellAlertTriggerLogRepository extends JpaRepository<SwellAlertTriggerLog, Long> {

    boolean existsByAlertIdAndForecastSnapshotId(Long alertId, Long forecastSnapshotId);

    List<SwellAlertTriggerLog> findByUserIdOrderByTriggeredAtDesc(Long userId);

    List<SwellAlertTriggerLog> findByAlertIdOrderByTriggeredAtDesc(Long alertId);
}