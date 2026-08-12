package br.com.george.commerce.exception;

import java.util.List;

public record ValidationErrorResponse(
        int status,
        String message,
        List<String> errors
) {
}
