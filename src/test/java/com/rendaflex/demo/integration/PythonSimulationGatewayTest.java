package com.rendaflex.demo.integration;

import com.rendaflex.demo.dto.internal.InternalExpenseSimulationRequest;
import com.rendaflex.demo.dto.internal.InternalExpenseSimulationResponse;
import com.rendaflex.demo.dto.request.SimulationRequest;
import com.rendaflex.demo.dto.response.SimulationResponse;
import com.rendaflex.demo.exception.ApiErrorCode;
import com.rendaflex.demo.exception.ModelServiceException;
import com.rendaflex.demo.integration.client.ExpenseSimulationClient;
import com.rendaflex.demo.mapper.SimulationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PythonSimulationGatewayTest {

    private SimulationMapper mapper;
    private ExpenseSimulationClient client;
    private PythonSimulationGateway gateway;

    @BeforeEach
    void setUp() {
        mapper = mock(SimulationMapper.class);
        client = mock(ExpenseSimulationClient.class);
        gateway = new PythonSimulationGateway(mapper, client);
    }

    @Test
    void shouldMapRequestCallPythonClientAndMapPublicResponse() {
        SimulationRequest publicRequest = mock(SimulationRequest.class);
        InternalExpenseSimulationRequest internalRequest =
                mock(InternalExpenseSimulationRequest.class);
        InternalExpenseSimulationResponse internalResponse =
                mock(InternalExpenseSimulationResponse.class);
        SimulationResponse expectedResponse = mock(SimulationResponse.class);

        when(mapper.toInternalRequest(publicRequest)).thenReturn(internalRequest);
        when(client.simulate(internalRequest)).thenReturn(internalResponse);
        when(mapper.toPublicResponse(publicRequest, internalResponse))
                .thenReturn(expectedResponse);

        SimulationResponse response = gateway.processSimulation(publicRequest);

        InOrder executionOrder = inOrder(mapper, client);
        executionOrder.verify(mapper).toInternalRequest(publicRequest);
        executionOrder.verify(client).simulate(internalRequest);
        executionOrder.verify(mapper).toPublicResponse(publicRequest, internalResponse);

        assertSame(expectedResponse, response);
    }

    @Test
    void shouldPropagateModelServiceFailureWithoutTryingToMapAResponse() {
        SimulationRequest publicRequest = mock(SimulationRequest.class);
        InternalExpenseSimulationRequest internalRequest =
                mock(InternalExpenseSimulationRequest.class);

        ModelServiceException expectedException = new ModelServiceException(
                ApiErrorCode.MODEL_SERVICE_UNAVAILABLE,
                "Serviço de modelo indisponível."
        );

        when(mapper.toInternalRequest(publicRequest)).thenReturn(internalRequest);
        when(client.simulate(internalRequest)).thenThrow(expectedException);

        ModelServiceException exception = assertThrows(
                ModelServiceException.class,
                () -> gateway.processSimulation(publicRequest)
        );

        assertSame(expectedException, exception);
        verify(mapper, never()).toPublicResponse(publicRequest, null);
    }
}