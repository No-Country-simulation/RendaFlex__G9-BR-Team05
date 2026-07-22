package com.rendaflex.demo;

import static org.assertj.core.api.Assertions.assertThat;

import com.rendaflex.demo.exception.ApiError;
import com.rendaflex.demo.exception.BusinessRuleException;
import com.rendaflex.demo.exception.GlobalExceptionHandler;
import com.rendaflex.demo.exception.ModelServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldReturnUnprocessableEntityForBusinessRuleException() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/financial-analyses");

        ResponseEntity<ApiError> response = handler.handleBusinessRule(
                new BusinessRuleException("Regra de negócio inválida."),
                request
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("BUSINESS_RULE_ERROR");
        assertThat(response.getBody().getPath()).isEqualTo("/api/v1/financial-analyses");
    }

    @Test
    void shouldReturnInternalErrorForModelServiceException() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/transactions/classify");

        ResponseEntity<ApiError> response = handler.handleModelService(
                new ModelServiceException("Falha interna do serviço Python."),
                request
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("INTERNAL_ERROR");
        assertThat(response.getBody().getMessage())
                .isEqualTo("Não foi possível processar a análise neste momento.");
    }
}
