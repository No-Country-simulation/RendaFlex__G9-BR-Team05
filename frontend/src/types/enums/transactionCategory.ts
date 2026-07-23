export const TransactionCategory = {
  FOOD: 'FOOD',
  TRANSPORT: 'TRANSPORT',
  HOUSING: 'HOUSING',
  HEALTH: 'HEALTH',
  EDUCATION: 'EDUCATION',
  ENTERTAINMENT: 'ENTERTAINMENT',
  SERVICES: 'SERVICES',
  TRANSFERS: 'TRANSFERS',
  DEBTS: 'DEBTS',
  FUEL: 'FUEL',
  BOOKS: 'BOOKS',
  RESTAURANTS: 'RESTAURANTS',
  FAST_FOOD: 'FAST_FOOD',
  OTHER: 'OTHER',
} as const

export type TransactionCategory =
  (typeof TransactionCategory)[keyof typeof TransactionCategory]
