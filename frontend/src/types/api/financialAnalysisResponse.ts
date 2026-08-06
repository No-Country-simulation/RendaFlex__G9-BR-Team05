import type { FinancialProfile } from '../enums/financialProfile'
import type {
  CategorySummary,
  CategoryPercentages,
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
  categoryPercentages: CategoryPercentages
  recommendations: Recommendation[]
}
