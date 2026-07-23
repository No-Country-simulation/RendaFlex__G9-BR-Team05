export const RecommendationPriority = {
  LOW: 'LOW',
  MEDIUM: 'MEDIUM',
  HIGH: 'HIGH',
} as const

export type RecommendationPriority =
  (typeof RecommendationPriority)[keyof typeof RecommendationPriority]
