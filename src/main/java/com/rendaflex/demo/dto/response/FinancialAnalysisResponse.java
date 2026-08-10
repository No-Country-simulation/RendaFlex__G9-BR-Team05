package com.rendaflex.demo.dto.response;

import com.rendaflex.demo.enums.FinancialProfile;
import com.rendaflex.demo.enums.TransactionCategory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record FinancialAnalysisResponse(
        FinancialProfile financialProfile,
        BigDecimal probability,
        FinancialMetrics metrics,
        List<ClassifiedTransaction> classifiedTransactions,
        Map<TransactionCategory, BigDecimal> categorySummary,
        Map<TransactionCategory, BigDecimal> categoryPercentages,
        List<Recommendation> recommendations
) {
}
