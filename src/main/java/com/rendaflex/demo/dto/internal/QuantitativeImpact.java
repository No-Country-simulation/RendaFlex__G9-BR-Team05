package com.rendaflex.demo.dto.internal;

import java.math.BigDecimal;
import java.util.Map;

public record QuantitativeImpact(
        Map<String, BigDecimal> metricVariations
) {
}