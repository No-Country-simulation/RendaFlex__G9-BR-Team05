package com.rendaflex.demo.dto.internal;

import java.util.List;

public record InternalExpenseSimulationResponse(
        InternalFinancialScenario currentScenario,
        InternalFinancialScenario projectedScenario,
        QuantitativeImpact quantitativeImpact,
        List<InternalRecommendation> recommendations
) {
}
