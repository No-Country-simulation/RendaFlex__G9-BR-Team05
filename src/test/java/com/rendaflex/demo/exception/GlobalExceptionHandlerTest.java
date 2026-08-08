package com.rendaflex.demo.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldHandleBusinessRuleException() {
        ResponseEntity<ApiError> response = handler.handleBusinessRule(
                new BusinessRuleException("Regra de negócio violada.")
        );

        assertEquals(HttpStatus.UNPROCESSABLE_CONTENT, response.getStatusCode());
        ApiError body = assertBody(response);
        assertEquals(ApiErrorCode.BUSINESS_RULE_ERROR, body.code());
        assertEquals("Regra de negócio violada.", body.message());
        assertTrue(body.fieldErrors().isEmpty());
    }

    @Test
    void shouldMapInvalidModelResponseToBadGateway() {
        assertModelServiceStatus(ApiErrorCode.MODEL_SERVICE_INVALID_RESPONSE, HttpStatus.BAD_GATEWAY);
    }

    @Test
    void shouldMapUnavailableModelServiceToServiceUnavailable() {
        assertModelServiceStatus(ApiErrorCode.MODEL_SERVICE_UNAVAILABLE, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Test
    void shouldMapModelServiceTimeoutToServiceUnavailable() {
        assertModelServiceStatus(ApiErrorCode.MODEL_SERVICE_TIMEOUT, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Test
    void shouldHandleUnexpectedExceptionWithoutExposingOriginalMessage() {
        String internalMessage = "Detalhe técnico confidencial";

        ResponseEntity<ApiError> response = handler.handleUnexpected(new Exception(internalMessage));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ApiError body = assertBody(response);
        assertEquals(ApiErrorCode.INTERNAL_ERROR, body.code());
        assertEquals("Ocorreu um erro interno inesperado.", body.message());
        assertFalse(body.message().contains(internalMessage));
        assertTrue(body.fieldErrors().isEmpty());
    }

    @Test
    void shouldAcceptOnlyModelServiceErrorCodes() {
        for (ApiErrorCode code : new ApiErrorCode[]{
                ApiErrorCode.MODEL_SERVICE_INVALID_RESPONSE,
                ApiErrorCode.MODEL_SERVICE_UNAVAILABLE,
                ApiErrorCode.MODEL_SERVICE_TIMEOUT
        }) {
            ModelServiceException exception = new ModelServiceException(code, "Falha no serviço de modelo.");
            assertEquals(code, exception.getCode());
        }

        assertThrows(
                IllegalArgumentException.class,
                () -> new ModelServiceException(ApiErrorCode.BUSINESS_RULE_ERROR, "Código inválido.")
        );
    }

    private void assertModelServiceStatus(ApiErrorCode code, HttpStatus expectedStatus) {
        ResponseEntity<ApiError> response = handler.handleModelService(
                new ModelServiceException(code, "Falha no serviço de modelo.")
        );

        assertEquals(expectedStatus, response.getStatusCode());
        ApiError body = assertBody(response);
        assertEquals(code, body.code());
        assertEquals("Falha no serviço de modelo.", body.message());
        assertTrue(body.fieldErrors().isEmpty());
    }

    private ApiError assertBody(ResponseEntity<ApiError> response) {
        ApiError body = response.getBody();
        assertNotNull(body);
        return body;
    }
}
