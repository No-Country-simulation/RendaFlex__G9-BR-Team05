package com.rendaflex.demo.dto.internal;

import java.math.BigDecimal;

public record InternalNewExpense(
        String description,
        BigDecimal totalAmount,
        Integer installmentCount,
        BigDecimal installmentAmount
) {
}