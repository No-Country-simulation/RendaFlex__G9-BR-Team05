package com.rendaflex.demo.dto.response;

import java.math.BigDecimal;


public record SimulatedExpenseResult(
        String description,
        BigDecimal totalAmount,
        Integer installmentCount,
        BigDecimal installmentAmount
    ) {}