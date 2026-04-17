package com.br.originalTruta.surfLife.surf.service;

import com.br.originalTruta.surfLife.auth.service.CurrentUserService;
import com.br.originalTruta.surfLife.common.exception.BusinessException;
import com.br.originalTruta.surfLife.common.exception.ResourceNotFoundException;
import com.br.originalTruta.surfLife.security.AuthenticatedUser;
import com.br.originalTruta.surfLife.surf.entity.FavoriteSpot;
import com.br.originalTruta.surfLife.surf.entity.Spot;
import com.br.originalTruta.surfLife.surf.record.FavoriteSpotRequest;
import com.br.originalTruta.surfLife.surf.record.FavoriteSpotResponse;
import com.br.originalTruta.surfLife.surf.repository.FavoriteSpotRepository;
import com.br.originalTruta.surfLife.user.entity.User;
import com.br.originalTruta.surfLife.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoriteSpotService {

    private final FavoriteSpotRepository favoriteSpotRepository;
    private final UserRepository userRepository;
    private final SpotService spotService;
    private final CurrentUserService currentUserService;

    public FavoriteSpotService(
            FavoriteSpotRepository favoriteSpotRepository,
            UserRepository userRepository,
            SpotService spotService,
            CurrentUserService currentUserService
    ) {
        this.favoriteSpotRepository = favoriteSpotRepository;
        this.userRepository = userRepository;
        this.spotService = spotService;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public FavoriteSpotResponse addMine(FavoriteSpotRequest request) {
        AuthenticatedUser currentUser = currentUserService.getAuthenticatedUser();

        if (favoriteSpotRepository.existsByUserIdAndSpotId(currentUser.getId(), request.spotId())) {
            throw new BusinessException("Spot is already in favorites.");
        }

        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found."));

        Spot spot = spotService.findEntityById(request.spotId());

        FavoriteSpot favoriteSpot = new FavoriteSpot();
        favoriteSpot.setUser(user);
        favoriteSpot.setSpot(spot);

        FavoriteSpot saved = favoriteSpotRepository.save(favoriteSpot);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<FavoriteSpotResponse> listMine() {
        AuthenticatedUser currentUser = currentUserService.getAuthenticatedUser();

        return favoriteSpotRepository.findByUserIdOrderBySpotNameAsc(currentUser.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void removeMine(Long spotId) {
        AuthenticatedUser currentUser = currentUserService.getAuthenticatedUser();

        FavoriteSpot favoriteSpot = favoriteSpotRepository.findByUserIdAndSpotId(currentUser.getId(), spotId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorite spot not found."));

        favoriteSpotRepository.delete(favoriteSpot);
    }

    private FavoriteSpotResponse toResponse(FavoriteSpot favoriteSpot) {
        Spot spot = favoriteSpot.getSpot();

        return new FavoriteSpotResponse(
                favoriteSpot.getId(),
                spot.getId(),
                spot.getName(),
                spot.getCity(),
                spot.getState(),
                spot.getCountry(),
                spot.getLatitude(),
                spot.getLongitude(),
                favoriteSpot.getCreatedAt()
        );
    }
}