package com.br.originalTruta.surfLife.surf.service;

import com.br.originalTruta.surfLife.common.exception.BusinessException;
import com.br.originalTruta.surfLife.surf.entity.ForecastSnapshot;
import com.br.originalTruta.surfLife.surf.entity.Spot;
import com.br.originalTruta.surfLife.surf.integration.openmeteo.OpenMeteoForecastClient;
import com.br.originalTruta.surfLife.surf.integration.openmeteo.record.OpenMeteoMarineHourlyResponse;
import com.br.originalTruta.surfLife.surf.integration.openmeteo.record.OpenMeteoWeatherHourlyResponse;
import com.br.originalTruta.surfLife.surf.record.ExternalForecastBatchImportResponse;
import com.br.originalTruta.surfLife.surf.record.ExternalForecastImportResponse;
import com.br.originalTruta.surfLife.surf.record.TideInfo;
import com.br.originalTruta.surfLife.surf.repository.ForecastSnapshotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExternalForecastSyncService {

    private static final String PROVIDER = "OPEN_METEO + WORLDTIDES";

    private final SpotService spotService;
    private final ForecastSnapshotRepository forecastSnapshotRepository;
    private final OpenMeteoForecastClient openMeteoForecastClient;
    private final RealTideService realTideService;

    public ExternalForecastSyncService(
            SpotService spotService,
            ForecastSnapshotRepository forecastSnapshotRepository,
            OpenMeteoForecastClient openMeteoForecastClient,
            RealTideService realTideService
    ) {
        this.spotService = spotService;
        this.forecastSnapshotRepository = forecastSnapshotRepository;
        this.openMeteoForecastClient = openMeteoForecastClient;
        this.realTideService = realTideService;
    }

    @Transactional
    public ExternalForecastImportResponse importLatestForSpot(Long spotId) {
        Spot spot = spotService.findEntityById(spotId);

        OpenMeteoMarineHourlyResponse marine = openMeteoForecastClient.getMarineForecast(
                spot.getLatitude(),
                spot.getLongitude()
        );

        OpenMeteoWeatherHourlyResponse weather = openMeteoForecastClient.getWeatherForecast(
                spot.getLatitude(),
                spot.getLongitude()
        );

        validateResponses(marine, weather);

        int bestIndex = resolveBestIndex(marine.hourly().time());

        ForecastSnapshot saved = buildAndSaveSnapshotIfNeeded(spot, marine, weather, bestIndex);

        return toSingleResponse(saved);
    }

    @Transactional
    public ExternalForecastBatchImportResponse importMultipleForSpot(Long spotId, Integer limit) {
        Spot spot = spotService.findEntityById(spotId);

        OpenMeteoMarineHourlyResponse marine = openMeteoForecastClient.getMarineForecast(
                spot.getLatitude(),
                spot.getLongitude()
        );

        OpenMeteoWeatherHourlyResponse weather = openMeteoForecastClient.getWeatherForecast(
                spot.getLatitude(),
                spot.getLongitude()
        );

        validateResponses(marine, weather);

        int maxAvailable = resolveAvailableSize(marine, weather);
        int requestedLimit = normalizeLimit(limit, maxAvailable);

        List<ExternalForecastImportResponse> imported = new ArrayList<>();

        for (int i = 0; i < requestedLimit; i++) {
            ForecastSnapshot saved = buildAndSaveSnapshotIfNeeded(spot, marine, weather, i);
            imported.add(toSingleResponse(saved));
        }

        return new ExternalForecastBatchImportResponse(
                spot.getId(),
                spot.getName(),
                PROVIDER,
                imported.size(),
                imported
        );
    }

    @Transactional
    public int importMultipleForSpotForScheduler(Long spotId, Integer limit) {
        return importMultipleForSpot(spotId, limit).importedCount();
    }

    private ForecastSnapshot buildAndSaveSnapshotIfNeeded(
            Spot spot,
            OpenMeteoMarineHourlyResponse marine,
            OpenMeteoWeatherHourlyResponse weather,
            int index
    ) {
        Double waveHeight = getRequiredValue(marine.hourly().waveHeight(), index, "wave_height");
        Double wavePeriodDouble = getRequiredValue(marine.hourly().wavePeriod(), index, "wave_period");
        Double swellDirectionDegrees = getRequiredValue(marine.hourly().waveDirection(), index, "wave_direction");
        Double windSpeed = getRequiredValue(weather.hourly().windSpeed10m(), index, "wind_speed_10m");
        Double windDirectionDegrees = getRequiredValue(weather.hourly().windDirection10m(), index, "wind_direction_10m");
        String observedAtRaw = getRequiredTime(marine.hourly().time(), index);
        OffsetDateTime observedAt = parseObservedAt(observedAtRaw);

        return forecastSnapshotRepository.findBySpotIdAndObservedAt(spot.getId(), observedAt)
                .orElseGet(() -> {
                    TideInfo tideInfo = realTideService.getTideInfo(
                            spot.getLatitude(),
                            spot.getLongitude(),
                            observedAt
                    );

                    ForecastSnapshot snapshot = new ForecastSnapshot();
                    snapshot.setSpot(spot);
                    snapshot.setWaveHeight(waveHeight);
                    snapshot.setWavePeriodSeconds((int) Math.round(wavePeriodDouble));
                    snapshot.setSwellDirection(degreesToDirection(swellDirectionDegrees));
                    snapshot.setWindSpeed(windSpeed);
                    snapshot.setWindDirection(degreesToDirection(windDirectionDegrees));
                    snapshot.setTideState(tideInfo.tideState());
                    snapshot.setTideHeight(tideInfo.tideHeight());
                    snapshot.setObservedAt(observedAt);

                    return forecastSnapshotRepository.save(snapshot);
                });
    }

    private ExternalForecastImportResponse toSingleResponse(ForecastSnapshot saved) {
        return new ExternalForecastImportResponse(
                saved.getSpot().getId(),
                saved.getSpot().getName(),
                saved.getId(),
                saved.getWaveHeight(),
                saved.getWavePeriodSeconds(),
                saved.getSwellDirection(),
                saved.getWindSpeed(),
                saved.getWindDirection(),
                saved.getTideState(),
                saved.getTideHeight(),
                saved.getObservedAt(),
                PROVIDER
        );
    }

    private void validateResponses(
            OpenMeteoMarineHourlyResponse marine,
            OpenMeteoWeatherHourlyResponse weather
    ) {
        if (marine == null || marine.hourly() == null || marine.hourly().time() == null || marine.hourly().time().isEmpty()) {
            throw new BusinessException("Marine forecast provider returned no hourly data.");
        }

        if (weather == null || weather.hourly() == null || weather.hourly().time() == null || weather.hourly().time().isEmpty()) {
            throw new BusinessException("Weather forecast provider returned no hourly data.");
        }
    }

    private int resolveBestIndex(List<String> times) {
        if (times == null || times.isEmpty()) {
            throw new BusinessException("No forecast times returned by external provider.");
        }

        return 0;
    }

    private int resolveAvailableSize(
            OpenMeteoMarineHourlyResponse marine,
            OpenMeteoWeatherHourlyResponse weather
    ) {
        int marineSize = marine.hourly().time().size();
        int weatherSize = weather.hourly().time().size();
        return Math.min(marineSize, weatherSize);
    }

    private int normalizeLimit(Integer limit, int maxAvailable) {
        if (maxAvailable <= 0) {
            throw new BusinessException("No forecast data available to import.");
        }

        if (limit == null) {
            return Math.min(12, maxAvailable);
        }

        if (limit < 1) {
            throw new BusinessException("Limit must be greater than zero.");
        }

        return Math.min(limit, maxAvailable);
    }

    private Double getRequiredValue(List<Double> values, int index, String fieldName) {
        if (values == null || values.size() <= index || values.get(index) == null) {
            throw new BusinessException("External provider did not return field: " + fieldName);
        }
        return values.get(index);
    }

    private String getRequiredTime(List<String> values, int index) {
        if (values == null || values.size() <= index || values.get(index) == null || values.get(index).isBlank()) {
            throw new BusinessException("External provider did not return field: time");
        }
        return values.get(index);
    }

    private OffsetDateTime parseObservedAt(String value) {
        if (value.endsWith("Z") || value.contains("+")) {
            return OffsetDateTime.parse(value);
        }

        if (value.length() == 16) {
            return OffsetDateTime.parse(value + ":00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        }

        if (value.length() == 19) {
            return OffsetDateTime.parse(value + "Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        }

        return OffsetDateTime.parse(value);
    }

    private String degreesToDirection(Double degrees) {
        if (degrees == null) {
            return "UNK";
        }

        double normalized = ((degrees % 360) + 360) % 360;

        if (normalized >= 337.5 || normalized < 22.5) return "N";
        if (normalized < 67.5) return "NE";
        if (normalized < 112.5) return "E";
        if (normalized < 157.5) return "SE";
        if (normalized < 202.5) return "S";
        if (normalized < 247.5) return "SW";
        if (normalized < 292.5) return "W";
        return "NW";
    }
}