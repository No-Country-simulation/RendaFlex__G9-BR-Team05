package com.rendaflex.demo.dto.internal;

import com.rendaflex.demo.enums.SavingFrequency;

import java.math.BigDecimal;
import java.util.List;

public record InternalFinancialAnalysisRequest(
        List<BigDecimal> incomeHistory,
        BigDecimal monthlyDebtPayments,
        BigDecimal otherFixedMonthlyExpenses,
        SavingFrequency savingFrequency,
        List<InternalExpenseTransaction> transactions
) {
}
