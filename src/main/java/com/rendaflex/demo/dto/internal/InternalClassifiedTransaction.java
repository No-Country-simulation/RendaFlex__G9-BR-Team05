package com.rendaflex.demo.dto.internal;

import com.rendaflex.demo.enums.TransactionCategory;

import java.math.BigDecimal;

public record InternalClassifiedTransaction(
        Integer sourceIndex,
        TransactionCategory predictedCategory,
        BigDecimal classificationProbability
) {
}
