package com.rendaflex.demo.dto.internal;

import java.math.BigDecimal;

public record InternalExpenseTransaction(
        Integer sourceIndex,
        String description,
        BigDecimal amount
) {
}
