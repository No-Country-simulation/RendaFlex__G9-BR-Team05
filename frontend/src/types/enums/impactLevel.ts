export const ImpactLevel = {
  LOW: 'LOW',
  MODERATE: 'MODERATE',
  HIGH: 'HIGH',
} as const

export type ImpactLevel =
  (typeof ImpactLevel)[keyof typeof ImpactLevel]
