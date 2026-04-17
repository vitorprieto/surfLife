CREATE TABLE swell_alert_trigger_logs (
    id BIGSERIAL PRIMARY KEY,
    alert_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    spot_id BIGINT NOT NULL,
    forecast_snapshot_id BIGINT NOT NULL,
    triggered_at TIMESTAMP WITH TIME ZONE NOT NULL,
    reason VARCHAR(500) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE,
    CONSTRAINT fk_swell_alert_trigger_logs_alert
        FOREIGN KEY (alert_id)
        REFERENCES swell_alerts (id),
    CONSTRAINT fk_swell_alert_trigger_logs_user
        FOREIGN KEY (user_id)
        REFERENCES users (id),
    CONSTRAINT fk_swell_alert_trigger_logs_spot
        FOREIGN KEY (spot_id)
        REFERENCES spots (id),
    CONSTRAINT fk_swell_alert_trigger_logs_snapshot
        FOREIGN KEY (forecast_snapshot_id)
        REFERENCES forecast_snapshots (id),
    CONSTRAINT uk_swell_alert_trigger_logs_alert_snapshot
        UNIQUE (alert_id, forecast_snapshot_id)
);