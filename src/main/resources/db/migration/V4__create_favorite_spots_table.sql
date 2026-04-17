CREATE TABLE favorite_spots (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    spot_id BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE,
    CONSTRAINT fk_favorite_spots_user
        FOREIGN KEY (user_id)
        REFERENCES users (id),
    CONSTRAINT fk_favorite_spots_spot
        FOREIGN KEY (spot_id)
        REFERENCES spots (id),
    CONSTRAINT uk_favorite_spots_user_spot
        UNIQUE (user_id, spot_id)
);