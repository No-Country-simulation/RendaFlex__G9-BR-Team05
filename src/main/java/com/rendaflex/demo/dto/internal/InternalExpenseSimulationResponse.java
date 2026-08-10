package com.rendaflex.demo.dto.internal;

public record InternalExpenseSimulationResponse(
        InternalFinancialScenario currentScenario,
        InternalFinancialScenario projectedScenario,
        QuantitativeImpact quantitativeImpact
) {
}