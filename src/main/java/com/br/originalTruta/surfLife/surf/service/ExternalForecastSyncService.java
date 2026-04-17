package com.br.originalTruta.surfLife.surf.service;

import com.br.originalTruta.surfLife.common.exception.BusinessException;
import com.br.originalTruta.surfLife.surf.entity.ForecastSnapshot;
import com.br.originalTruta.surfLife.surf.entity.Spot;
import com.br.originalTruta.surfLife.surf.integration.openmeteo.OpenMeteoForecastClient;
import com.br.originalTruta.surfLife.surf.integration.openmeteo.record.OpenMeteoMarineHourlyResponse;
import com.br.originalTruta.surfLife.surf.integration.openmeteo.record.OpenMeteoWeatherHourlyResponse;
import com.br.originalTruta.surfLife.surf.record.ExternalForecastImportResponse;
import com.br.originalTruta.surfLife.surf.repository.ForecastSnapshotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class ExternalForecastSyncService {

    private final SpotService spotService;
    private final ForecastSnapshotRepository forecastSnapshotRepository;
    private final OpenMeteoForecastClient openMeteoForecastClient;

    public ExternalForecastSyncService(
            SpotService spotService,
            ForecastSnapshotRepository forecastSnapshotRepository,
            OpenMeteoForecastClient openMeteoForecastClient
    ) {
        this.spotService = spotService;
        this.forecastSnapshotRepository = forecastSnapshotRepository;
        this.openMeteoForecastClient = openMeteoForecastClient;
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

        int bestIndex = resolveBestIndex(marine.hourly().time());

        Double waveHeight = getRequiredValue(marine.hourly().waveHeight(), bestIndex, "wave_height");
        Double wavePeriodDouble = getRequiredValue(marine.hourly().wavePeriod(), bestIndex, "wave_period");
        Double swellDirectionDegrees = getRequiredValue(marine.hourly().waveDirection(), bestIndex, "wave_direction");
        Double windSpeed = getRequiredValue(weather.hourly().windSpeed10m(), bestIndex, "wind_speed_10m");
        Double windDirectionDegrees = getRequiredValue(weather.hourly().windDirection10m(), bestIndex, "wind_direction_10m");

        ForecastSnapshot snapshot = new ForecastSnapshot();
        snapshot.setSpot(spot);
        snapshot.setWaveHeight(waveHeight);
        snapshot.setWavePeriodSeconds((int) Math.round(wavePeriodDouble));
        snapshot.setSwellDirection(degreesToDirection(swellDirectionDegrees));
        snapshot.setWindSpeed(windSpeed);
        snapshot.setWindDirection(degreesToDirection(windDirectionDegrees));
        snapshot.setTideState("MID");
        snapshot.setTideHeight(0.0);
        snapshot.setObservedAt(parseObservedAt(marine.hourly().time().get(bestIndex)));

        ForecastSnapshot saved = forecastSnapshotRepository.save(snapshot);

        return new ExternalForecastImportResponse(
                spot.getId(),
                spot.getName(),
                saved.getId(),
                saved.getWaveHeight(),
                saved.getWavePeriodSeconds(),
                saved.getSwellDirection(),
                saved.getWindSpeed(),
                saved.getWindDirection(),
                saved.getTideState(),
                saved.getTideHeight(),
                saved.getObservedAt(),
                "OPEN_METEO"
        );
    }

    private int resolveBestIndex(List<String> times) {
        if (times == null || times.isEmpty()) {
            throw new BusinessException("No forecast times returned by external provider.");
        }

        return 0;
    }

    private Double getRequiredValue(List<Double> values, int index, String fieldName) {
        if (values == null || values.size() <= index || values.get(index) == null) {
            throw new BusinessException("External provider did not return field: " + fieldName);
        }
        return values.get(index);
    }

    private OffsetDateTime parseObservedAt(String value) {
        return OffsetDateTime.parse(value + ":00Z");
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