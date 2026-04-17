package com.br.originalTruta.surfLife.surf.service;

import com.br.originalTruta.surfLife.common.exception.BusinessException;
import com.br.originalTruta.surfLife.common.exception.ResourceNotFoundException;
import com.br.originalTruta.surfLife.surf.entity.Spot;
import com.br.originalTruta.surfLife.surf.record.CreateSpotRequest;
import com.br.originalTruta.surfLife.surf.record.SpotResponse;
import com.br.originalTruta.surfLife.surf.repository.SpotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SpotService {

    private final SpotRepository spotRepository;

    public SpotService(SpotRepository spotRepository) {
        this.spotRepository = spotRepository;
    }

    @Transactional
    public SpotResponse create(CreateSpotRequest request) {
        String normalizedName = normalize(request.name());

        if (spotRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new BusinessException("Spot name is already in use.");
        }

        Spot spot = new Spot();
        spot.setName(normalizedName);
        spot.setCity(request.city());
        spot.setState(request.state());
        spot.setCountry(request.country());
        spot.setLatitude(request.latitude());
        spot.setLongitude(request.longitude());
        spot.activate();

        Spot saved = spotRepository.save(spot);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<SpotResponse> listActive() {
        return spotRepository.findByActiveTrueOrderByNameAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Spot findEntityById(Long id) {
        return spotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Spot not found."));
    }

    private SpotResponse toResponse(Spot spot) {
        return new SpotResponse(
                spot.getId(),
                spot.getName(),
                spot.getCity(),
                spot.getState(),
                spot.getCountry(),
                spot.getLatitude(),
                spot.getLongitude(),
                spot.isActive(),
                spot.getCreatedAt(),
                spot.getUpdatedAt()
        );
    }

    private String normalize(String value) {
        return value == null ? null : value.trim().replaceAll("\\s+", " ");
    }
}