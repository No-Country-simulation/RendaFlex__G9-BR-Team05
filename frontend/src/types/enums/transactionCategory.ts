export const TransactionCategory = {
  FOOD: 'FOOD',
  TRANSPORT: 'TRANSPORT',
  HOUSING: 'HOUSING',
  HEALTH: 'HEALTH',
  EDUCATION: 'EDUCATION',
  ENTERTAINMENT: 'ENTERTAINMENT',
  SERVICES: 'SERVICES',
  OTHER: 'OTHER',
} as const

export type TransactionCategory =
  (typeof TransactionCategory)[keyof typeof TransactionCategory]
