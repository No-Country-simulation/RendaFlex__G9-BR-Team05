import type { FinancialProfile } from '../enums/financialProfile'
import type {
  CategorySummary,
  ClassifiedTransaction,
  FinancialMetrics,
  Recommendation,
} from './common'

export type FinancialAnalysisResponse = {
  financialProfile: FinancialProfile
  probability: number
  metrics: FinancialMetrics
  classifiedTransactions: ClassifiedTransaction[]
  categorySummary: CategorySummary
  recommendations: Recommendation[]
}
