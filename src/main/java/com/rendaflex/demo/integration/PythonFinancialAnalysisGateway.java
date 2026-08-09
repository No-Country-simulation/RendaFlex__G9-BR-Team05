package com.rendaflex.demo.integration;

import com.rendaflex.demo.dto.internal.InternalFinancialAnalysisRequest;
import com.rendaflex.demo.dto.internal.InternalFinancialAnalysisResponse;
import com.rendaflex.demo.dto.request.FinancialAnalysisRequest;
import com.rendaflex.demo.dto.response.FinancialAnalysisResponse;
import com.rendaflex.demo.integration.client.FinancialAnalysisClient;
import com.rendaflex.demo.mapper.FinancialAnalysisMapper;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PythonFinancialAnalysisGateway implements FinancialAnalysisGateway {

    private final FinancialAnalysisMapper mapper;
    private final FinancialAnalysisClient client;

    public PythonFinancialAnalysisGateway(
            FinancialAnalysisMapper mapper,
            FinancialAnalysisClient client
    ) {
        this.mapper = Objects.requireNonNull(mapper, "FinancialAnalysisMapper must not be null.");
        this.client = Objects.requireNonNull(client, "FinancialAnalysisClient must not be null.");
    }

    @Override
    public FinancialAnalysisResponse analyze(FinancialAnalysisRequest request) {
        InternalFinancialAnalysisRequest internalRequest = mapper.toInternalRequest(request);
        InternalFinancialAnalysisResponse internalResponse = client.analyze(internalRequest);
        return mapper.toPublicResponse(request, internalResponse);
    }
}
