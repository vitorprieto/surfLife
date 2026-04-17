package com.br.originalTruta.surfLife.surf.entity;

import com.br.originalTruta.surfLife.common.entity.BaseAuditableEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "spots")
public class Spot extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120, unique = true)
    private String name;

    @Column(nullable = false, length = 120)
    private String city;

    @Column(nullable = false, length = 120)
    private String state;

    @Column(nullable = false, length = 120)
    private String country;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = normalize(name);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = normalize(city);
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = normalize(state);
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = normalize(country);
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public boolean isActive() {
        return active;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    private String normalize(String value) {
        return value == null ? null : value.trim().replaceAll("\\s+", " ");
    }
}