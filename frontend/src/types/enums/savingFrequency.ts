export const SavingFrequency = {
  RARELY: 'RARELY',
  SOMETIMES: 'SOMETIMES',
  OFTEN: 'OFTEN',
} as const

export type SavingFrequency =
  (typeof SavingFrequency)[keyof typeof SavingFrequency]
