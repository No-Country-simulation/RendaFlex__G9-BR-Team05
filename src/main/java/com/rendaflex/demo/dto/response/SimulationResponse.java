package com.rendaflex.demo.dto.response;

import com.rendaflex.demo.enums.FinancialProfile;
import com.rendaflex.demo.enums.ImpactLevel;

import java.util.List;

public record SimulationResponse(
    SimulatedExpenseResult newExpense,
    FinancialScenario currentScenario,
    FinancialScenario projectedScenario,
    Boolean profileChanged,
    Boolean financialHealthWorsened,
    ImpactLevel impactLevel,
    List<Recommendation> recommendations
){}

