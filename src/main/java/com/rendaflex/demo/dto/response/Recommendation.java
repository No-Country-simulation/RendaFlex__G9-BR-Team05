package com.rendaflex.demo.dto.response;

import com.rendaflex.demo.enums.RecommendationPriority;

/**
 * Recommendation exposed in the public financial analysis response.
 */
public record Recommendation(
        RecommendationPriority priority,
        String message
) {
}
