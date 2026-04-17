package com.br.originalTruta.surfLife.surf.service;

import com.br.originalTruta.surfLife.auth.service.CurrentUserService;
import com.br.originalTruta.surfLife.common.exception.ResourceNotFoundException;
import com.br.originalTruta.surfLife.security.AuthenticatedUser;
import com.br.originalTruta.surfLife.surf.entity.SurfProfile;
import com.br.originalTruta.surfLife.surf.repository.SurfProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SurfProfileQueryService {

    private final SurfProfileRepository surfProfileRepository;
    private final CurrentUserService currentUserService;

    public SurfProfileQueryService(
            SurfProfileRepository surfProfileRepository,
            CurrentUserService currentUserService
    ) {
        this.surfProfileRepository = surfProfileRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional(readOnly = true)
    public SurfProfile getMineEntity() {
        AuthenticatedUser currentUser = currentUserService.getAuthenticatedUser();

        return surfProfileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Surf profile not found for authenticated user."));
    }
}