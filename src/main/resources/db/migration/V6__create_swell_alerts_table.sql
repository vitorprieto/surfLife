CREATE TABLE swell_alerts (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    user_id BIGINT NOT NULL,
    spot_id BIGINT NOT NULL,
    min_wave_height DOUBLE PRECISION NOT NULL,
    max_wave_height DOUBLE PRECISION,
    min_period_seconds INTEGER NOT NULL,
    max_wind_speed DOUBLE PRECISION,
    preferred_tides VARCHAR(200),
    preferred_wind_directions VARCHAR(200),
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE,
    CONSTRAINT fk_swell_alerts_user
        FOREIGN KEY (user_id)
        REFERENCES users (id),
    CONSTRAINT fk_swell_alerts_spot
        FOREIGN KEY (spot_id)
        REFERENCES spots (id),
    CONSTRAINT uk_swell_alerts_user_spot_name
        UNIQUE (user_id, spot_id, name)
);