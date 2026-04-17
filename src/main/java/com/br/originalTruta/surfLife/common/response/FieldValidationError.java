package com.br.originalTruta.surfLife.common.response;

public record FieldValidationError(
        String field,
        String message
) {
}
