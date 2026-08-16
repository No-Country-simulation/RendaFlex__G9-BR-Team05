package com.rendaflex.demo.dto.response;

import com.rendaflex.demo.enums.FinancialProfile;

import java.math.BigDecimal;

    public record FinancialScenario(
        FinancialProfile financialProfile,
        BigDecimal probability,
        FinancialMetrics metrics
    ) {}