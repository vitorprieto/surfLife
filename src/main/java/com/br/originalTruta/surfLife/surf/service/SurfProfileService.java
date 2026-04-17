package com.br.originalTruta.surfLife.surf.service;

import com.br.originalTruta.surfLife.auth.service.CurrentUserService;
import com.br.originalTruta.surfLife.common.exception.BusinessException;
import com.br.originalTruta.surfLife.common.exception.ResourceNotFoundException;
import com.br.originalTruta.surfLife.security.AuthenticatedUser;
import com.br.originalTruta.surfLife.surf.entity.SurfProfile;
import com.br.originalTruta.surfLife.surf.record.SurfProfileResponse;
import com.br.originalTruta.surfLife.surf.record.UpsertSurfProfileRequest;
import com.br.originalTruta.surfLife.surf.repository.SurfProfileRepository;
import com.br.originalTruta.surfLife.user.entity.User;
import com.br.originalTruta.surfLife.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SurfProfileService {

    private final SurfProfileRepository surfProfileRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    public SurfProfileService(
            SurfProfileRepository surfProfileRepository,
            UserRepository userRepository,
            CurrentUserService currentUserService
    ) {
        this.surfProfileRepository = surfProfileRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional(readOnly = true)
    public SurfProfileResponse getMine() {
        AuthenticatedUser currentUser = currentUserService.getAuthenticatedUser();

        SurfProfile profile = surfProfileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Surf profile not found for authenticated user."));

        return toResponse(profile);
    }

    @Transactional
    public SurfProfileResponse upsertMine(UpsertSurfProfileRequest request) {
        validate(request);

        AuthenticatedUser currentUser = currentUserService.getAuthenticatedUser();

        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found."));

        SurfProfile profile = surfProfileRepository.findByUserId(currentUser.getId())
                .orElseGet(SurfProfile::new);

        profile.setUser(user);
        profile.setLevel(request.level());
        profile.setMinWaveHeight(request.minWaveHeight());
        profile.setMaxWaveHeight(request.maxWaveHeight());
        profile.setMinPeriodSeconds(request.minPeriodSeconds());
        profile.setPreferredTides(request.preferredTides());
        profile.setOffshoreDirections(request.offshoreDirections());

        SurfProfile saved = surfProfileRepository.save(profile);
        return toResponse(saved);
    }

    private void validate(UpsertSurfProfileRequest request) {
        if (request.minWaveHeight() != null
                && request.maxWaveHeight() != null
                && request.minWaveHeight() > request.maxWaveHeight()) {
            throw new BusinessException("Minimum wave height cannot be greater than maximum wave height.");
        }
    }

    private SurfProfileResponse toResponse(SurfProfile profile) {
        return new SurfProfileResponse(
                profile.getId(),
                profile.getUser().getId(),
                profile.getLevel(),
                profile.getMinWaveHeight(),
                profile.getMaxWaveHeight(),
                profile.getMinPeriodSeconds(),
                profile.getPreferredTides(),
                profile.getOffshoreDirections(),
                profile.getCreatedAt(),
                profile.getUpdatedAt()
        );
    }
}