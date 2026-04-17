package com.br.originalTruta.surfLife.surf.entity;

import com.br.originalTruta.surfLife.common.entity.BaseAuditableEntity;
import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(
        name = "forecast_snapshots",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_forecast_snapshots_spot_observed_at",
                        columnNames = {"spot_id", "observed_at"}
                )
        }
)
public class ForecastSnapshot extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "spot_id", nullable = false)
    private Spot spot;

    @Column(name = "wave_height", nullable = false)
    private Double waveHeight;

    @Column(name = "wave_period_seconds", nullable = false)
    private Integer wavePeriodSeconds;

    @Column(name = "swell_direction", nullable = false, length = 20)
    private String swellDirection;

    @Column(name = "wind_speed", nullable = false)
    private Double windSpeed;

    @Column(name = "wind_direction", nullable = false, length = 20)
    private String windDirection;

    @Column(name = "tide_state", nullable = false, length = 20)
    private String tideState;

    @Column(name = "tide_height", nullable = false)
    private Double tideHeight;

    @Column(name = "observed_at", nullable = false)
    private OffsetDateTime observedAt;

    public Long getId() {
        return id;
    }

    public Spot getSpot() {
        return spot;
    }

    public void setSpot(Spot spot) {
        this.spot = spot;
    }

    public Double getWaveHeight() {
        return waveHeight;
    }

    public void setWaveHeight(Double waveHeight) {
        this.waveHeight = waveHeight;
    }

    public Integer getWavePeriodSeconds() {
        return wavePeriodSeconds;
    }

    public void setWavePeriodSeconds(Integer wavePeriodSeconds) {
        this.wavePeriodSeconds = wavePeriodSeconds;
    }

    public String getSwellDirection() {
        return swellDirection;
    }

    public void setSwellDirection(String swellDirection) {
        this.swellDirection = normalize(swellDirection);
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = normalize(windDirection);
    }

    public String getTideState() {
        return tideState;
    }

    public void setTideState(String tideState) {
        this.tideState = normalize(tideState);
    }

    public Double getTideHeight() {
        return tideHeight;
    }

    public void setTideHeight(Double tideHeight) {
        this.tideHeight = tideHeight;
    }

    public OffsetDateTime getObservedAt() {
        return observedAt;
    }

    public void setObservedAt(OffsetDateTime observedAt) {
        this.observedAt = observedAt;
    }

    private String normalize(String value) {
        return value == null ? null : value.trim().toUpperCase();
    }
}