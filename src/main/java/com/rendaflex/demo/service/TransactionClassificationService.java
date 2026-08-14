package com.rendaflex.demo.service;

import com.rendaflex.demo.dto.request.TransactionClassificationRequest;
import com.rendaflex.demo.dto.response.TransactionClassificationResponse;
import com.rendaflex.demo.integration.TransactionClassificationGateway;
import org.springframework.stereotype.Service;

@Service
public class TransactionClassificationService {

    private final TransactionClassificationGateway transactionClassificationGateway;

    public TransactionClassificationService(
            TransactionClassificationGateway transactionClassificationGateway
    ) {
        this.transactionClassificationGateway = transactionClassificationGateway;
    }

    public TransactionClassificationResponse classify(
            TransactionClassificationRequest request
    ) {
        return transactionClassificationGateway.classify(request);
    }
}
