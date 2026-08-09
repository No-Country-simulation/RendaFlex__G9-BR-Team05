package com.rendaflex.demo.integration;

import com.rendaflex.demo.dto.internal.InternalFinancialAnalysisRequest;
import com.rendaflex.demo.dto.internal.InternalFinancialAnalysisResponse;
import com.rendaflex.demo.dto.request.FinancialAnalysisRequest;
import com.rendaflex.demo.dto.response.FinancialAnalysisResponse;
import com.rendaflex.demo.exception.ApiErrorCode;
import com.rendaflex.demo.exception.ModelServiceException;
import com.rendaflex.demo.integration.client.FinancialAnalysisClient;
import com.rendaflex.demo.mapper.FinancialAnalysisMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PythonFinancialAnalysisGatewayTest {

    private FinancialAnalysisMapper mapper;
    private FinancialAnalysisClient client;
    private PythonFinancialAnalysisGateway gateway;

    @BeforeEach
    void setUp() {
        mapper = mock(FinancialAnalysisMapper.class);
        client = mock(FinancialAnalysisClient.class);
        gateway = new PythonFinancialAnalysisGateway(mapper, client);
    }

    @Test
    void shouldMapRequestCallPythonClientAndMapPublicResponse() {
        FinancialAnalysisRequest publicRequest = mock(FinancialAnalysisRequest.class);
        InternalFinancialAnalysisRequest internalRequest = mock(InternalFinancialAnalysisRequest.class);
        InternalFinancialAnalysisResponse internalResponse = mock(InternalFinancialAnalysisResponse.class);
        FinancialAnalysisResponse expectedResponse = mock(FinancialAnalysisResponse.class);

        when(mapper.toInternalRequest(publicRequest)).thenReturn(internalRequest);
        when(client.analyze(internalRequest)).thenReturn(internalResponse);
        when(mapper.toPublicResponse(publicRequest, internalResponse, List.of()))
                .thenReturn(expectedResponse);

        FinancialAnalysisResponse response = gateway.analyze(publicRequest);

        InOrder executionOrder = inOrder(mapper, client);
        executionOrder.verify(mapper).toInternalRequest(publicRequest);
        executionOrder.verify(client).analyze(internalRequest);
        executionOrder.verify(mapper).toPublicResponse(publicRequest, internalResponse, List.of());
        assertSame(expectedResponse, response);
    }

    @Test
    void shouldPropagateModelServiceFailureWithoutTryingToMapAResponse() {
        FinancialAnalysisRequest publicRequest = mock(FinancialAnalysisRequest.class);
        InternalFinancialAnalysisRequest internalRequest = mock(InternalFinancialAnalysisRequest.class);
        ModelServiceException expectedException = new ModelServiceException(
                ApiErrorCode.MODEL_SERVICE_UNAVAILABLE,
                "Serviço de modelo indisponível."
        );

        when(mapper.toInternalRequest(publicRequest)).thenReturn(internalRequest);
        when(client.analyze(internalRequest)).thenThrow(expectedException);

        ModelServiceException exception = assertThrows(
                ModelServiceException.class,
                () -> gateway.analyze(publicRequest)
        );

        assertSame(expectedException, exception);
        verify(mapper, never()).toPublicResponse(
                publicRequest,
                null,
                List.of()
        );
    }
}
