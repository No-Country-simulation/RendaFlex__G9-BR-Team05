package com.rendaflex.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Comparator;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException exception) {
        List<ApiFieldError> fieldErrors = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> new ApiFieldError(error.getField(), error.getDefaultMessage()))
                .sorted(Comparator.comparing(ApiFieldError::field))
                .toList();

        return response(
                HttpStatus.BAD_REQUEST,
                ApiErrorCode.VALIDATION_ERROR,
                "Existem campos inválidos na requisição.",
                fieldErrors
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleNotReadable(HttpMessageNotReadableException exception) {
        return response(
                HttpStatus.BAD_REQUEST,
                ApiErrorCode.VALIDATION_ERROR,
                "A requisição possui formato ou valores inválidos.",
                List.of()
        );
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ApiError> handleBusinessRule(BusinessRuleException exception) {
        return response(
                HttpStatus.UNPROCESSABLE_CONTENT,
                ApiErrorCode.BUSINESS_RULE_ERROR,
                exception.getMessage(),
                List.of()
        );
    }

    @ExceptionHandler(ModelServiceException.class)
    public ResponseEntity<ApiError> handleModelService(ModelServiceException exception) {
        HttpStatus status = switch (exception.getCode()) {
            case MODEL_SERVICE_INVALID_RESPONSE -> HttpStatus.BAD_GATEWAY;
            case MODEL_SERVICE_UNAVAILABLE, MODEL_SERVICE_TIMEOUT -> HttpStatus.SERVICE_UNAVAILABLE;
            default -> throw new IllegalArgumentException("Código inválido para falha do serviço de modelo.");
        };

        return response(status, exception.getCode(), exception.getMessage(), List.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(Exception exception) {
        return response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ApiErrorCode.INTERNAL_ERROR,
                "Ocorreu um erro interno inesperado.",
                List.of()
        );
    }

    private ResponseEntity<ApiError> response(
            HttpStatus status,
            ApiErrorCode code,
            String message,
            List<ApiFieldError> fieldErrors
    ) {
        return ResponseEntity.status(status).body(new ApiError(code, message, fieldErrors));
    }
}
