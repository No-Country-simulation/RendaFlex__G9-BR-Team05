package com.rendaflex.demo.integration;

import com.rendaflex.demo.dto.internal.InternalClassificationTransaction;
import com.rendaflex.demo.dto.internal.InternalTransactionClassificationRequest;
import com.rendaflex.demo.dto.internal.InternalTransactionClassificationResponse;
import com.rendaflex.demo.dto.request.TransactionClassificationRequest;
import com.rendaflex.demo.dto.response.TransactionClassificationResponse;
import com.rendaflex.demo.exception.ApiErrorCode;
import com.rendaflex.demo.exception.ModelServiceException;
import com.rendaflex.demo.integration.client.TransactionClassificationClient;
import com.rendaflex.demo.mapper.TransactionClassificationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PythonTransactionClassificationGatewayTest {

    private TransactionClassificationMapper mapper;
    private TransactionClassificationClient client;
    private PythonTransactionClassificationGateway gateway;

    @BeforeEach
    void setUp() {
        mapper = mock(TransactionClassificationMapper.class);
        client = mock(TransactionClassificationClient.class);
        gateway = new PythonTransactionClassificationGateway(mapper, client);
    }

    @Test
    void shouldMapRequestCallPythonClientAndMapPublicResponse() {
        TransactionClassificationRequest publicRequest =
                mock(TransactionClassificationRequest.class);

        InternalTransactionClassificationRequest internalRequest =
                mock(InternalTransactionClassificationRequest.class);

        InternalTransactionClassificationResponse internalResponse =
                mock(InternalTransactionClassificationResponse.class);

        TransactionClassificationResponse expectedResponse =
                mock(TransactionClassificationResponse.class);

        when(internalRequest.transactions())
                .thenReturn(List.of(
                        mock(InternalClassificationTransaction.class)
                ));

        when(mapper.toInternalRequest(publicRequest))
                .thenReturn(internalRequest);

        when(client.classify(internalRequest))
                .thenReturn(internalResponse);

        when(mapper.toPublicResponse(publicRequest, internalResponse))
                .thenReturn(expectedResponse);

        TransactionClassificationResponse response =
                gateway.classify(publicRequest);

        InOrder executionOrder = inOrder(mapper, client);

        executionOrder.verify(mapper)
                .toInternalRequest(publicRequest);

        executionOrder.verify(client)
                .classify(internalRequest);

        executionOrder.verify(mapper)
                .toPublicResponse(publicRequest, internalResponse);

        assertSame(expectedResponse, response);
    }

    @Test
    void shouldSkipPythonWhenThereAreNoClassifiableTransactions() {
        TransactionClassificationRequest publicRequest =
                mock(TransactionClassificationRequest.class);

        InternalTransactionClassificationRequest internalRequest =
                new InternalTransactionClassificationRequest(List.of());

        TransactionClassificationResponse expectedResponse =
                mock(TransactionClassificationResponse.class);

        when(mapper.toInternalRequest(publicRequest))
                .thenReturn(internalRequest);

        when(mapper.toPublicResponse(
                eq(publicRequest),
                argThat(internalResponse ->
                        internalResponse != null
                                && internalResponse.transactions() != null
                                && internalResponse.transactions().isEmpty()
                )
        )).thenReturn(expectedResponse);

        TransactionClassificationResponse response =
                gateway.classify(publicRequest);

        assertSame(expectedResponse, response);

        verify(client, never())
                .classify(any(InternalTransactionClassificationRequest.class));

        verify(mapper).toPublicResponse(
                eq(publicRequest),
                argThat(internalResponse ->
                        internalResponse != null
                                && internalResponse.transactions() != null
                                && internalResponse.transactions().isEmpty()
                )
        );
    }

    @Test
    void shouldPropagateModelServiceFailureWithoutMappingPublicResponse() {
        TransactionClassificationRequest publicRequest =
                mock(TransactionClassificationRequest.class);

        InternalTransactionClassificationRequest internalRequest =
                mock(InternalTransactionClassificationRequest.class);

        when(internalRequest.transactions())
                .thenReturn(List.of(
                        mock(InternalClassificationTransaction.class)
                ));

        ModelServiceException expectedException =
                new ModelServiceException(
                        ApiErrorCode.MODEL_SERVICE_UNAVAILABLE,
                        "Serviço de classificação indisponível."
                );

        when(mapper.toInternalRequest(publicRequest))
                .thenReturn(internalRequest);

        when(client.classify(internalRequest))
                .thenThrow(expectedException);

        ModelServiceException exception = assertThrows(
                ModelServiceException.class,
                () -> gateway.classify(publicRequest)
        );

        assertSame(expectedException, exception);

        verify(mapper, never())
                .toPublicResponse(
                        eq(publicRequest),
                        any(InternalTransactionClassificationResponse.class)
                );
    }

    @Test
    void shouldRejectNullMapperInConstructor() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new PythonTransactionClassificationGateway(
                        null,
                        client
                )
        );

        assertThat(exception)
                .hasMessage(
                        "TransactionClassificationMapper must not be null."
                );
    }

    @Test
    void shouldRejectNullClientInConstructor() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new PythonTransactionClassificationGateway(
                        mapper,
                        null
                )
        );

        assertThat(exception)
                .hasMessage(
                        "TransactionClassificationClient must not be null."
                );
    }
}
