package com.br.originalTruta.surfLife.surf.entity;

import com.br.originalTruta.surfLife.common.entity.BaseAuditableEntity;
import com.br.originalTruta.surfLife.user.entity.User;
import jakarta.persistence.*;

@Entity
@Table(name = "surf_profiles")
public class SurfProfile extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private SurfLevel level;

    @Column(name = "min_wave_height", nullable = false)
    private Double minWaveHeight;

    @Column(name = "max_wave_height", nullable = false)
    private Double maxWaveHeight;

    @Column(name = "min_period_seconds", nullable = false)
    private Integer minPeriodSeconds;

    @Column(name = "preferred_tides", length = 200)
    private String preferredTides;

    @Column(name = "offshore_directions", length = 200)
    private String offshoreDirections;

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SurfLevel getLevel() {
        return level;
    }

    public void setLevel(SurfLevel level) {
        this.level = level;
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

    public String getPreferredTides() {
        return preferredTides;
    }

    public void setPreferredTides(String preferredTides) {
        this.preferredTides = normalize(preferredTides);
    }

    public String getOffshoreDirections() {
        return offshoreDirections;
    }

    public void setOffshoreDirections(String offshoreDirections) {
        this.offshoreDirections = normalize(offshoreDirections);
    }

    private String normalize(String value) {
        return value == null ? null : value.trim().replaceAll("\\s+", " ");
    }
}