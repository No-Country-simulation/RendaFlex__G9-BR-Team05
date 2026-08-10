package com.rendaflex.demo.dto.internal;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record InternalClassificationTransaction(
        Integer sourceIndex,
        String description,
        BigDecimal amount
) {
}
