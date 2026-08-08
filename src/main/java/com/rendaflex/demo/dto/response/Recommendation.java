package com.rendaflex.demo.dto.response;

import com.rendaflex.demo.enums.RecommendationPriority;

public record Recommendation(
        RecommendationPriority priority,
        String message
) {
}
