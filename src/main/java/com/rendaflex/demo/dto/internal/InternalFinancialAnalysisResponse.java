package com.rendaflex.demo.dto.internal;

import com.rendaflex.demo.enums.FinancialProfile;
import com.rendaflex.demo.enums.TransactionCategory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record InternalFinancialAnalysisResponse(
        FinancialProfile financialProfile,
        BigDecimal probability,
        InternalFinancialMetrics metrics,
        List<InternalClassifiedTransaction> classifiedTransactions,
        Map<TransactionCategory, BigDecimal> categorySummary,
        Map<TransactionCategory, BigDecimal> categoryPercentages,
        List<InternalRecommendation> recommendations
) {
}
