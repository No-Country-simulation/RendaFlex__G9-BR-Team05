package com.rendaflex.demo.dto.response;

import com.rendaflex.demo.enums.TransactionCategory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record TransactionClassificationResponse(
        List<ClassifiedTransaction> transactions,
        Map<TransactionCategory, BigDecimal> categorySummary,
        Map<TransactionCategory, BigDecimal> categoryPercentages
) {
}
