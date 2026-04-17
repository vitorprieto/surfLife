package com.br.originalTruta.surfLife.surf.service;

import com.br.originalTruta.surfLife.common.exception.ResourceNotFoundException;
import com.br.originalTruta.surfLife.surf.entity.ForecastSnapshot;
import com.br.originalTruta.surfLife.surf.entity.Spot;
import com.br.originalTruta.surfLife.surf.record.CreateForecastSnapshotRequest;
import com.br.originalTruta.surfLife.surf.record.ForecastSnapshotResponse;
import com.br.originalTruta.surfLife.surf.repository.ForecastSnapshotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ForecastSnapshotService {

    private final ForecastSnapshotRepository forecastSnapshotRepository;
    private final SpotService spotService;

    public ForecastSnapshotService(
            ForecastSnapshotRepository forecastSnapshotRepository,
            SpotService spotService
    ) {
        this.forecastSnapshotRepository = forecastSnapshotRepository;
        this.spotService = spotService;
    }

    @Transactional
    public ForecastSnapshotResponse create(CreateForecastSnapshotRequest request) {
        Spot spot = spotService.findEntityById(request.spotId());

        ForecastSnapshot snapshot = new ForecastSnapshot();
        snapshot.setSpot(spot);
        snapshot.setWaveHeight(request.waveHeight());
        snapshot.setWavePeriodSeconds(request.wavePeriodSeconds());
        snapshot.setSwellDirection(request.swellDirection());
        snapshot.setWindSpeed(request.windSpeed());
        snapshot.setWindDirection(request.windDirection());
        snapshot.setTideState(request.tideState());
        snapshot.setTideHeight(request.tideHeight());
        snapshot.setObservedAt(request.observedAt());

        ForecastSnapshot saved = forecastSnapshotRepository.save(snapshot);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ForecastSnapshot findLatestEntityBySpotId(Long spotId) {
        return forecastSnapshotRepository.findTopBySpotIdOrderByObservedAtDesc(spotId)
                .orElseThrow(() -> new ResourceNotFoundException("Forecast snapshot not found for spot."));
    }

    private ForecastSnapshotResponse toResponse(ForecastSnapshot snapshot) {
        return new ForecastSnapshotResponse(
                snapshot.getId(),
                snapshot.getSpot().getId(),
                snapshot.getSpot().getName(),
                snapshot.getWaveHeight(),
                snapshot.getWavePeriodSeconds(),
                snapshot.getSwellDirection(),
                snapshot.getWindSpeed(),
                snapshot.getWindDirection(),
                snapshot.getTideState(),
                snapshot.getTideHeight(),
                snapshot.getObservedAt(),
                snapshot.getCreatedAt(),
                snapshot.getUpdatedAt()
        );
    }
}