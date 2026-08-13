package com.rendaflex.demo.service;

import com.rendaflex.demo.dto.request.FinancialAnalysisRequest;
import com.rendaflex.demo.dto.response.FinancialAnalysisResponse;
import com.rendaflex.demo.integration.FinancialAnalysisGateway;
import com.rendaflex.demo.validation.FinancialAnalysisValidator;
import org.springframework.stereotype.Service;

@Service
public class FinancialAnalysisService {

    private final FinancialAnalysisValidator financialAnalysisValidator;
    private final FinancialAnalysisGateway financialAnalysisGateway;

    public FinancialAnalysisService(
            FinancialAnalysisValidator financialAnalysisValidator,
            FinancialAnalysisGateway financialAnalysisGateway
    ) {
        this.financialAnalysisValidator = financialAnalysisValidator;
        this.financialAnalysisGateway = financialAnalysisGateway;
    }

    public FinancialAnalysisResponse analyze(FinancialAnalysisRequest request) {
        financialAnalysisValidator.validate(request);
        return financialAnalysisGateway.analyze(request);
    }
}
