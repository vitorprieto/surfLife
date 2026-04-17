package com.br.originalTruta.surfLife.common.response;

import java.time.OffsetDateTime;

public record ApiResponse<T>(
        OffsetDateTime timestamp,
        String message,
        T data
) {
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(OffsetDateTime.now(), message, data);
    }
}
