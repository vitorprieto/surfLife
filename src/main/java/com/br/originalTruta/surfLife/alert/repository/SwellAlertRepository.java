package com.br.originalTruta.surfLife.alert.repository;

import com.br.originalTruta.surfLife.alert.entity.SwellAlert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SwellAlertRepository extends JpaRepository<SwellAlert, Long> {

    boolean existsByUserIdAndSpotIdAndNameIgnoreCase(Long userId, Long spotId, String name);

    List<SwellAlert> findByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<SwellAlert> findByIdAndUserId(Long id, Long userId);
}