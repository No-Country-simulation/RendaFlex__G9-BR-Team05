package com.rendaflex.demo.dto.response;

import com.rendaflex.demo.enums.FinancialProfile;
import com.rendaflex.demo.enums.TransactionCategory;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Public response returned by the financial analysis endpoint.
 */
public record FinancialAnalysisResponse(
        FinancialProfile financialProfile,
        BigDecimal probability,
        FinancialMetrics metrics,
        List<ClassifiedTransaction> classifiedTransactions,
        Map<TransactionCategory, BigDecimal> categorySummary,
        List<Recommendation> recommendations
) {
}
