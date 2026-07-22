package com.rendaflex.demo.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        List<FieldError> fieldErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldError(
                        error.getField(),
                        error.getCode() == null ? "INVALID_VALUE" : error.getCode(),
                        error.getDefaultMessage() == null ? "Valor inválido" : error.getDefaultMessage(),
                        error.getRejectedValue()
                ))
                .toList();

        return badRequest(
                "A requisição contém campos inválidos.",
                request.getRequestURI(),
                fieldErrors
        );
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class,
            ConstraintViolationException.class
    })
    public ResponseEntity<ApiError> handleMalformedRequest(
            Exception exception,
            HttpServletRequest request
    ) {
        return badRequest(
                "A requisição possui formato ou valores inválidos.",
                request.getRequestURI(),
                Collections.emptyList()
        );
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ApiError> handleBusinessRule(
            BusinessRuleException exception,
            HttpServletRequest request
    ) {
        ApiError apiError = createError(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "BUSINESS_RULE_ERROR",
                exception.getMessage(),
                request.getRequestURI(),
                Collections.emptyList()
        );

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(apiError);
    }

    @ExceptionHandler(ModelServiceException.class)
    public ResponseEntity<ApiError> handleModelService(
            ModelServiceException exception,
            HttpServletRequest request
    ) {
        ApiError apiError = createError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_ERROR",
                "Não foi possível processar a análise neste momento.",
                request.getRequestURI(),
                Collections.emptyList()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(
            Exception exception,
            HttpServletRequest request
    ) {
        ApiError apiError = createError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_ERROR",
                "Ocorreu um erro interno inesperado.",
                request.getRequestURI(),
                Collections.emptyList()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

    private ResponseEntity<ApiError> badRequest(
            String message,
            String path,
            List<FieldError> fieldErrors
    ) {
        ApiError apiError = createError(
                HttpStatus.BAD_REQUEST,
                "VALIDATION_ERROR",
                message,
                path,
                fieldErrors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    private ApiError createError(
            HttpStatus status,
            String code,
            String message,
            String path,
            List<FieldError> fieldErrors
    ) {
        return new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                code,
                message,
                path,
                fieldErrors
        );
    }
}
