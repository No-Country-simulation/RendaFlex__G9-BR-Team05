import type {
  CategoryPercentages,
  CategorySummary,
  ClassifiedTransaction,
} from './common'
import type { TransactionType } from '../enums/transactionType'

export type ClassificationTransactionInput = {
  description: string
  amount?: number
  date?: string
  type?: TransactionType
}

export type TransactionClassificationRequest = {
  transactions: ClassificationTransactionInput[]
}

export type TransactionClassificationResponse = {
  transactions: ClassifiedTransaction[]
  categorySummary: CategorySummary
  categoryPercentages: CategoryPercentages
}
