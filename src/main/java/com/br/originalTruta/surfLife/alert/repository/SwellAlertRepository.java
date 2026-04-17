package com.br.originalTruta.surfLife.alert.repository;

import com.br.originalTruta.surfLife.alert.entity.SwellAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SwellAlertRepository extends JpaRepository<SwellAlert, Long> {

    boolean existsByUserIdAndSpotIdAndNameIgnoreCase(Long userId, Long spotId, String name);

    List<SwellAlert> findByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<SwellAlert> findByIdAndUserId(Long id, Long userId);

    List<SwellAlert> findBySpotIdAndEnabledTrue(Long spotId);

    @Query("""
            select distinct sa.spot.id
            from SwellAlert sa
            where sa.enabled = true
            """)
    List<Long> findDistinctEnabledSpotIds();
}