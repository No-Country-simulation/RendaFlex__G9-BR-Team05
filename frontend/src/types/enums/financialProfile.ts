export const FinancialProfile = {
  HEALTHY: 'HEALTHY',
  UNDER_OBSERVATION: 'UNDER_OBSERVATION',
  AT_RISK: 'AT_RISK',
} as const

export type FinancialProfile =
  (typeof FinancialProfile)[keyof typeof FinancialProfile]
