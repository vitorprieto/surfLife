package com.br.originalTruta.surfLife.alert.entity;

import com.br.originalTruta.surfLife.common.entity.BaseAuditableEntity;
import com.br.originalTruta.surfLife.surf.entity.Spot;
import com.br.originalTruta.surfLife.user.entity.User;
import jakarta.persistence.*;

@Entity
@Table(
        name = "swell_alerts",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_swell_alerts_user_spot_name",
                        columnNames = {"user_id", "spot_id", "name"}
                )
        }
)
public class SwellAlert extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "spot_id", nullable = false)
    private Spot spot;

    @Column(name = "min_wave_height", nullable = false)
    private Double minWaveHeight;

    @Column(name = "max_wave_height")
    private Double maxWaveHeight;

    @Column(name = "min_period_seconds", nullable = false)
    private Integer minPeriodSeconds;

    @Column(name = "max_wind_speed")
    private Double maxWindSpeed;

    @Column(name = "preferred_tides", length = 200)
    private String preferredTides;

    @Column(name = "preferred_wind_directions", length = 200)
    private String preferredWindDirections;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = normalize(name);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Spot getSpot() {
        return spot;
    }

    public void setSpot(Spot spot) {
        this.spot = spot;
    }

    public Double getMinWaveHeight() {
        return minWaveHeight;
    }

    public void setMinWaveHeight(Double minWaveHeight) {
        this.minWaveHeight = minWaveHeight;
    }

    public Double getMaxWaveHeight() {
        return maxWaveHeight;
    }

    public void setMaxWaveHeight(Double maxWaveHeight) {
        this.maxWaveHeight = maxWaveHeight;
    }

    public Integer getMinPeriodSeconds() {
        return minPeriodSeconds;
    }

    public void setMinPeriodSeconds(Integer minPeriodSeconds) {
        this.minPeriodSeconds = minPeriodSeconds;
    }

    public Double getMaxWindSpeed() {
        return maxWindSpeed;
    }

    public void setMaxWindSpeed(Double maxWindSpeed) {
        this.maxWindSpeed = maxWindSpeed;
    }

    public String getPreferredTides() {
        return preferredTides;
    }

    public void setPreferredTides(String preferredTides) {
        this.preferredTides = normalizeCsv(preferredTides);
    }

    public String getPreferredWindDirections() {
        return preferredWindDirections;
    }

    public void setPreferredWindDirections(String preferredWindDirections) {
        this.preferredWindDirections = normalizeCsv(preferredWindDirections);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

    private String normalize(String value) {
        return value == null ? null : value.trim().replaceAll("\\s+", " ");
    }

    private String normalizeCsv(String value) {
        return value == null ? null : value.trim().replaceAll("\\s+", "").toUpperCase();
    }
}