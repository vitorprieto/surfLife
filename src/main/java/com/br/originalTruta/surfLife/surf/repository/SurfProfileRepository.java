package com.br.originalTruta.surfLife.surf.repository;

import com.br.originalTruta.surfLife.surf.entity.SurfProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SurfProfileRepository extends JpaRepository<SurfProfile, Long> {

    Optional<SurfProfile> findByUserId(Long userId);
}