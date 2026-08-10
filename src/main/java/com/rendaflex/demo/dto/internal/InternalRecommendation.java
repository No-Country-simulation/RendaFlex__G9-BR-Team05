package com.rendaflex.demo.dto.internal;

import com.rendaflex.demo.enums.RecommendationPriority;

public record InternalRecommendation(
        RecommendationPriority priority,
        String message
) {
}
