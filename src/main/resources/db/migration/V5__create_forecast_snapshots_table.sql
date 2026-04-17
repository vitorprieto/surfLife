CREATE TABLE forecast_snapshots (
    id BIGSERIAL PRIMARY KEY,
    spot_id BIGINT NOT NULL,
    wave_height DOUBLE PRECISION NOT NULL,
    wave_period_seconds INTEGER NOT NULL,
    swell_direction VARCHAR(20) NOT NULL,
    wind_speed DOUBLE PRECISION NOT NULL,
    wind_direction VARCHAR(20) NOT NULL,
    tide_state VARCHAR(20) NOT NULL,
    tide_height DOUBLE PRECISION NOT NULL,
    observed_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE,
    CONSTRAINT fk_forecast_snapshots_spot
        FOREIGN KEY (spot_id)
        REFERENCES spots (id)
);