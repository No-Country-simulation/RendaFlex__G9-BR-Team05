export const SavingFrequency = {
  LOW: 'LOW',
  MEDIUM: 'MEDIUM',
  HIGH: 'HIGH',
} as const

export type SavingFrequency =
  (typeof SavingFrequency)[keyof typeof SavingFrequency]
