CREATE TABLE surf_profiles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    level VARCHAR(30) NOT NULL,
    min_wave_height DOUBLE PRECISION NOT NULL,
    max_wave_height DOUBLE PRECISION NOT NULL,
    min_period_seconds INTEGER NOT NULL,
    preferred_tides VARCHAR(200),
    offshore_directions VARCHAR(200),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE,
    CONSTRAINT fk_surf_profiles_user
        FOREIGN KEY (user_id)
        REFERENCES users (id)
);