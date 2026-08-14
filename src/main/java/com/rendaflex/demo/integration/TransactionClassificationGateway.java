package com.rendaflex.demo.integration;

import com.rendaflex.demo.dto.request.TransactionClassificationRequest;
import com.rendaflex.demo.dto.response.TransactionClassificationResponse;

public interface TransactionClassificationGateway {

    TransactionClassificationResponse classify(
            TransactionClassificationRequest request
    );
}
