package com.rendaflex.demo.controller;

import com.rendaflex.demo.dto.request.TransactionClassificationRequest;
import com.rendaflex.demo.dto.response.TransactionClassificationResponse;
import com.rendaflex.demo.service.TransactionClassificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactions/classify")
public class TransactionClassificationController {

    private final TransactionClassificationService transactionClassificationService;

    public TransactionClassificationController(
            TransactionClassificationService transactionClassificationService
    ) {
        this.transactionClassificationService = transactionClassificationService;
    }

    @PostMapping
    public ResponseEntity<TransactionClassificationResponse> classify(
            @Valid @RequestBody TransactionClassificationRequest request
    ) {
        TransactionClassificationResponse response =
                transactionClassificationService.classify(request);

        return ResponseEntity.ok(response);
    }
}
