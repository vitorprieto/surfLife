package com.br.originalTruta.surfLife.surf.repository;

import com.br.originalTruta.surfLife.surf.entity.Spot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpotRepository extends JpaRepository<Spot, Long> {

    boolean existsByNameIgnoreCase(String name);

    Optional<Spot> findByNameIgnoreCase(String name);

    List<Spot> findByActiveTrueOrderByNameAsc();
}