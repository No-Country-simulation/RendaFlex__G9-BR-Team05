package com.rendaflex.demo.integration;

import com.rendaflex.demo.dto.request.FinancialAnalysisRequest;
import com.rendaflex.demo.dto.response.FinancialAnalysisResponse;

public interface FinancialAnalysisGateway {

    FinancialAnalysisResponse analyze(FinancialAnalysisRequest request);
}
