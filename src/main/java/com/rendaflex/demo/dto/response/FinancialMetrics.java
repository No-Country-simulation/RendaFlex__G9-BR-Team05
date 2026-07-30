package com.rendaflex.demo.dto.response;

import java.math.BigDecimal;

/**
 * Financial metrics exposed in the public API response.
 */
public record FinancialMetrics(
        BigDecimal averageIncome,
        BigDecimal incomeVariationCoefficientPercentage,
        BigDecimal debtRatioPercentage,
        BigDecimal fixedCommitmentPercentage
) {
}
