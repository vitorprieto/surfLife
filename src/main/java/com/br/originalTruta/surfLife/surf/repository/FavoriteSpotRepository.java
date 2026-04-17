package com.br.originalTruta.surfLife.surf.repository;

import com.br.originalTruta.surfLife.surf.entity.FavoriteSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteSpotRepository extends JpaRepository<FavoriteSpot, Long> {

    boolean existsByUserIdAndSpotId(Long userId, Long spotId);

    Optional<FavoriteSpot> findByUserIdAndSpotId(Long userId, Long spotId);

    List<FavoriteSpot> findByUserIdOrderBySpotNameAsc(Long userId);
}