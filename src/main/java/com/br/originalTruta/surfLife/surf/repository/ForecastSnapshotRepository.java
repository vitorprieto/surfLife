package com.br.originalTruta.surfLife.surf.repository;

import com.br.originalTruta.surfLife.surf.entity.ForecastSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface ForecastSnapshotRepository extends JpaRepository<ForecastSnapshot, Long> {

    Optional<ForecastSnapshot> findTopBySpotIdOrderByObservedAtDesc(Long spotId);

    List<ForecastSnapshot> findBySpotIdAndObservedAtBetweenOrderByObservedAtAsc(
            Long spotId,
            OffsetDateTime start,
            OffsetDateTime end
    );
}