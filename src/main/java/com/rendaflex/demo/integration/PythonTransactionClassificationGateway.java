package com.rendaflex.demo.integration;

import com.rendaflex.demo.dto.internal.InternalTransactionClassificationRequest;
import com.rendaflex.demo.dto.internal.InternalTransactionClassificationResponse;
import com.rendaflex.demo.dto.request.TransactionClassificationRequest;
import com.rendaflex.demo.dto.response.TransactionClassificationResponse;
import com.rendaflex.demo.integration.client.TransactionClassificationClient;
import com.rendaflex.demo.mapper.TransactionClassificationMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class PythonTransactionClassificationGateway
        implements TransactionClassificationGateway {

    private final TransactionClassificationMapper mapper;
    private final TransactionClassificationClient client;

    public PythonTransactionClassificationGateway(
            TransactionClassificationMapper mapper,
            TransactionClassificationClient client
    ) {
        this.mapper = Objects.requireNonNull(
                mapper,
                "TransactionClassificationMapper must not be null."
        );

        this.client = Objects.requireNonNull(
                client,
                "TransactionClassificationClient must not be null."
        );
    }

    @Override
    public TransactionClassificationResponse classify(
            TransactionClassificationRequest request
    ) {
        InternalTransactionClassificationRequest internalRequest =
                mapper.toInternalRequest(request);

        if (internalRequest.transactions().isEmpty()) {
            InternalTransactionClassificationResponse emptyResponse =
                    new InternalTransactionClassificationResponse(List.of());

            return mapper.toPublicResponse(request, emptyResponse);
        }

        InternalTransactionClassificationResponse internalResponse =
                client.classify(internalRequest);

        return mapper.toPublicResponse(request, internalResponse);
    }
}
