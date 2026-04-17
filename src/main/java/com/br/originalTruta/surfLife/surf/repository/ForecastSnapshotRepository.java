package com.br.originalTruta.surfLife.surf.repository;

import com.br.originalTruta.surfLife.surf.entity.ForecastSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ForecastSnapshotRepository extends JpaRepository<ForecastSnapshot, Long> {

    Optional<ForecastSnapshot> findTopBySpotIdOrderByObservedAtDesc(Long spotId);
}