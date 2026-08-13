package com.rendaflex.demo.service;

import com.rendaflex.demo.dto.request.FinancialAnalysisRequest;
import com.rendaflex.demo.dto.response.FinancialAnalysisResponse;
import com.rendaflex.demo.exception.ApiErrorCode;
import com.rendaflex.demo.exception.BusinessRuleException;
import com.rendaflex.demo.exception.ModelServiceException;
import com.rendaflex.demo.integration.FinancialAnalysisGateway;
import com.rendaflex.demo.validation.FinancialAnalysisValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FinancialAnalysisServiceTest {

    private FinancialAnalysisValidator validator;
    private FinancialAnalysisGateway gateway;
    private FinancialAnalysisService service;

    @BeforeEach
    void setUp() {
        validator = mock(FinancialAnalysisValidator.class);
        gateway = mock(FinancialAnalysisGateway.class);
        service = new FinancialAnalysisService(validator, gateway);
    }

    @Test
    void shouldValidateCallGatewayAndReturnItsResponse() {
        FinancialAnalysisRequest request = mock(FinancialAnalysisRequest.class);
        FinancialAnalysisResponse expectedResponse = mock(FinancialAnalysisResponse.class);
        when(gateway.analyze(request)).thenReturn(expectedResponse);

        FinancialAnalysisResponse response = service.analyze(request);

        InOrder executionOrder = inOrder(validator, gateway);
        executionOrder.verify(validator).validate(request);
        executionOrder.verify(gateway).analyze(request);
        assertSame(expectedResponse, response);
    }

    @Test
    void shouldPropagateBusinessRuleExceptionWithoutCallingGateway() {
        FinancialAnalysisRequest request = mock(FinancialAnalysisRequest.class);
        BusinessRuleException expectedException = new BusinessRuleException("Requisição inválida.");
        doThrow(expectedException).when(validator).validate(request);

        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> service.analyze(request)
        );

        assertSame(expectedException, exception);
        verify(gateway, never()).analyze(request);
    }

    @Test
    void shouldPropagateModelServiceExceptionWithoutChanges() {
        FinancialAnalysisRequest request = mock(FinancialAnalysisRequest.class);
        ModelServiceException expectedException = new ModelServiceException(
                ApiErrorCode.MODEL_SERVICE_UNAVAILABLE,
                "Serviço de modelo indisponível."
        );
        when(gateway.analyze(request)).thenThrow(expectedException);

        ModelServiceException exception = assertThrows(
                ModelServiceException.class,
                () -> service.analyze(request)
        );

        verify(validator).validate(request);
        assertSame(expectedException, exception);
    }
}
