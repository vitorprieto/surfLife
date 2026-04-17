package com.br.originalTruta.surfLife.alert.service;

import com.br.originalTruta.surfLife.alert.entity.SwellAlert;
import com.br.originalTruta.surfLife.alert.record.CreateSwellAlertRequest;
import com.br.originalTruta.surfLife.alert.record.SwellAlertResponse;
import com.br.originalTruta.surfLife.alert.record.UpdateSwellAlertRequest;
import com.br.originalTruta.surfLife.alert.repository.SwellAlertRepository;
import com.br.originalTruta.surfLife.auth.service.CurrentUserService;
import com.br.originalTruta.surfLife.common.exception.BusinessException;
import com.br.originalTruta.surfLife.common.exception.ResourceNotFoundException;
import com.br.originalTruta.surfLife.security.AuthenticatedUser;
import com.br.originalTruta.surfLife.surf.entity.Spot;
import com.br.originalTruta.surfLife.surf.service.SpotService;
import com.br.originalTruta.surfLife.user.entity.User;
import com.br.originalTruta.surfLife.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SwellAlertService {

    private final SwellAlertRepository swellAlertRepository;
    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;
    private final SpotService spotService;

    public SwellAlertService(
            SwellAlertRepository swellAlertRepository,
            CurrentUserService currentUserService,
            UserRepository userRepository,
            SpotService spotService
    ) {
        this.swellAlertRepository = swellAlertRepository;
        this.currentUserService = currentUserService;
        this.userRepository = userRepository;
        this.spotService = spotService;
    }

    @Transactional
    public SwellAlertResponse createMine(CreateSwellAlertRequest request) {
        validateCreate(request);

        AuthenticatedUser currentUser = currentUserService.getAuthenticatedUser();

        String normalizedName = normalize(request.name());

        if (swellAlertRepository.existsByUserIdAndSpotIdAndNameIgnoreCase(
                currentUser.getId(),
                request.spotId(),
                normalizedName
        )) {
            throw new BusinessException("An alert with this name already exists for this spot.");
        }

        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found."));

        Spot spot = spotService.findEntityById(request.spotId());

        SwellAlert alert = new SwellAlert();
        alert.setName(normalizedName);
        alert.setUser(user);
        alert.setSpot(spot);
        alert.setMinWaveHeight(request.minWaveHeight());
        alert.setMaxWaveHeight(request.maxWaveHeight());
        alert.setMinPeriodSeconds(request.minPeriodSeconds());
        alert.setMaxWindSpeed(request.maxWindSpeed());
        alert.setPreferredTides(request.preferredTides());
        alert.setPreferredWindDirections(request.preferredWindDirections());

        if (Boolean.FALSE.equals(request.enabled())) {
            alert.disable();
        } else {
            alert.enable();
        }

        SwellAlert saved = swellAlertRepository.save(alert);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<SwellAlertResponse> listMine() {
        AuthenticatedUser currentUser = currentUserService.getAuthenticatedUser();

        return swellAlertRepository.findByUserIdOrderByCreatedAtDesc(currentUser.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public SwellAlertResponse updateMine(Long alertId, UpdateSwellAlertRequest request) {
        validateUpdate(request);

        AuthenticatedUser currentUser = currentUserService.getAuthenticatedUser();

        SwellAlert alert = swellAlertRepository.findByIdAndUserId(alertId, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Swell alert not found."));

        alert.setName(normalize(request.name()));
        alert.setMinWaveHeight(request.minWaveHeight());
        alert.setMaxWaveHeight(request.maxWaveHeight());
        alert.setMinPeriodSeconds(request.minPeriodSeconds());
        alert.setMaxWindSpeed(request.maxWindSpeed());
        alert.setPreferredTides(request.preferredTides());
        alert.setPreferredWindDirections(request.preferredWindDirections());

        if (Boolean.TRUE.equals(request.enabled())) {
            alert.enable();
        } else if (Boolean.FALSE.equals(request.enabled())) {
            alert.disable();
        }

        SwellAlert saved = swellAlertRepository.save(alert);
        return toResponse(saved);
    }

    @Transactional
    public void deleteMine(Long alertId) {
        AuthenticatedUser currentUser = currentUserService.getAuthenticatedUser();

        SwellAlert alert = swellAlertRepository.findByIdAndUserId(alertId, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Swell alert not found."));

        swellAlertRepository.delete(alert);
    }

    private void validateCreate(CreateSwellAlertRequest request) {
        if (request.maxWaveHeight() != null && request.minWaveHeight() > request.maxWaveHeight()) {
            throw new BusinessException("Minimum wave height cannot be greater than maximum wave height.");
        }
    }

    private void validateUpdate(UpdateSwellAlertRequest request) {
        if (request.minWaveHeight() != null
                && request.maxWaveHeight() != null
                && request.minWaveHeight() > request.maxWaveHeight()) {
            throw new BusinessException("Minimum wave height cannot be greater than maximum wave height.");
        }
    }

    private SwellAlertResponse toResponse(SwellAlert alert) {
        return new SwellAlertResponse(
                alert.getId(),
                alert.getName(),
                alert.getUser().getId(),
                alert.getSpot().getId(),
                alert.getSpot().getName(),
                alert.getMinWaveHeight(),
                alert.getMaxWaveHeight(),
                alert.getMinPeriodSeconds(),
                alert.getMaxWindSpeed(),
                alert.getPreferredTides(),
                alert.getPreferredWindDirections(),
                alert.isEnabled(),
                alert.getCreatedAt(),
                alert.getUpdatedAt()
        );
    }

    private String normalize(String value) {
        return value == null ? null : value.trim().replaceAll("\\s+", " ");
    }
}