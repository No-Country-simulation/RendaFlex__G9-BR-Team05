package com.rendaflex.demo.dto.internal;

import com.rendaflex.demo.enums.FinancialProfile;

import java.math.BigDecimal;

public record InternalFinancialScenario(
        FinancialProfile financialProfile,
        BigDecimal probability,
        InternalFinancialMetrics metrics
) {
}