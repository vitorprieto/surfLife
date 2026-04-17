package com.br.originalTruta.surfLife.common.response;

import java.time.Instant;
import java.util.List;

public record ApiErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        List<FieldValidationError> fieldErrors
) {
    public static ApiErrorResponse of(
            int status,
            String error,
            String message,
            String path,
            List<FieldValidationError> fieldErrors
    ) {
        return new ApiErrorResponse(
                Instant.now(),
                status,
                error,
                message,
                path,
                fieldErrors
        );
    }
}