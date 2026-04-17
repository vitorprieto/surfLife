package com.br.originalTruta.surfLife.alert.entity;

import com.br.originalTruta.surfLife.common.entity.BaseAuditableEntity;
import com.br.originalTruta.surfLife.surf.entity.ForecastSnapshot;
import com.br.originalTruta.surfLife.surf.entity.Spot;
import com.br.originalTruta.surfLife.user.entity.User;
import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "swell_alert_trigger_logs")
public class SwellAlertTriggerLog extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "alert_id", nullable = false)
    private SwellAlert alert;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "spot_id", nullable = false)
    private Spot spot;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "forecast_snapshot_id", nullable = false)
    private ForecastSnapshot forecastSnapshot;

    @Column(name = "triggered_at", nullable = false)
    private OffsetDateTime triggeredAt;

    @Column(name = "reason", nullable = false, length = 500)
    private String reason;

    public Long getId() {
        return id;
    }

    public SwellAlert getAlert() {
        return alert;
    }

    public void setAlert(SwellAlert alert) {
        this.alert = alert;
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

    public ForecastSnapshot getForecastSnapshot() {
        return forecastSnapshot;
    }

    public void setForecastSnapshot(ForecastSnapshot forecastSnapshot) {
        this.forecastSnapshot = forecastSnapshot;
    }

    public OffsetDateTime getTriggeredAt() {
        return triggeredAt;
    }

    public void setTriggeredAt(OffsetDateTime triggeredAt) {
        this.triggeredAt = triggeredAt;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = normalize(reason);
    }

    private String normalize(String value) {
        return value == null ? null : value.trim().replaceAll("\\s+", " ");
    }
}