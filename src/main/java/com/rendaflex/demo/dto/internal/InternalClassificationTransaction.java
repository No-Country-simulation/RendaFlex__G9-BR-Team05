package com.rendaflex.demo.dto.internal;

import java.math.BigDecimal;

public record InternalClassificationTransaction(
        Integer sourceIndex,
        String description,
        BigDecimal amount
) {
}
