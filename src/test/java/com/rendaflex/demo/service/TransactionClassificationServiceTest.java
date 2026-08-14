package com.rendaflex.demo.service;

import com.rendaflex.demo.dto.request.TransactionClassificationRequest;
import com.rendaflex.demo.dto.response.TransactionClassificationResponse;
import com.rendaflex.demo.exception.ApiErrorCode;
import com.rendaflex.demo.exception.ModelServiceException;
import com.rendaflex.demo.integration.TransactionClassificationGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TransactionClassificationServiceTest {

    private TransactionClassificationGateway gateway;
    private TransactionClassificationService service;

    @BeforeEach
    void setUp() {
        gateway = mock(TransactionClassificationGateway.class);
        service = new TransactionClassificationService(gateway);
    }

    @Test
    void shouldCallGatewayAndReturnItsResponse() {
        TransactionClassificationRequest request =
                mock(TransactionClassificationRequest.class);

        TransactionClassificationResponse expectedResponse =
                mock(TransactionClassificationResponse.class);

        when(gateway.classify(request))
                .thenReturn(expectedResponse);

        TransactionClassificationResponse response =
                service.classify(request);

        verify(gateway).classify(request);
        assertSame(expectedResponse, response);
    }

    @Test
    void shouldPropagateModelServiceExceptionWithoutChanges() {
        TransactionClassificationRequest request =
                mock(TransactionClassificationRequest.class);

        ModelServiceException expectedException =
                new ModelServiceException(
                        ApiErrorCode.MODEL_SERVICE_UNAVAILABLE,
                        "Serviço de classificação indisponível."
                );

        when(gateway.classify(request))
                .thenThrow(expectedException);

        ModelServiceException exception = assertThrows(
                ModelServiceException.class,
                () -> service.classify(request)
        );

        assertSame(expectedException, exception);
    }
}
