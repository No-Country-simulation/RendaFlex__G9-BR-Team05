package com.rendaflex.demo.controller;

import com.rendaflex.demo.dto.request.FinancialAnalysisRequest;
import com.rendaflex.demo.dto.response.FinancialAnalysisResponse;
import com.rendaflex.demo.service.FinancialAnalysisService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/financial-analyses")
public class FinancialAnalysisController {

    private final FinancialAnalysisService financialAnalysisService;

    public FinancialAnalysisController(FinancialAnalysisService financialAnalysisService) {
        this.financialAnalysisService = financialAnalysisService;
    }

    @PostMapping
    public ResponseEntity<FinancialAnalysisResponse> analyze(
            @Valid @RequestBody FinancialAnalysisRequest request
    ) {
        FinancialAnalysisResponse response = financialAnalysisService.analyze(request);
        return ResponseEntity.ok(response);
    }
}
