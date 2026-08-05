import type { FinancialProfile } from '../enums/financialProfile'
import type { FinancialMetrics, Recommendation } from './common'

export type ExpenseSimulationResponse = {
  simulatedExpense: {
    description: string
    totalAmount: number
    installments: number
    monthlyAmount: number
  }
  currentProfile: FinancialProfile
  projectedProfile: FinancialProfile
  currentProbability: number
  projectedProbability: number
  profileChanged: boolean
  monthlyInstallmentAmount: number
  currentMetrics: FinancialMetrics
  projectedMetrics: FinancialMetrics
  recommendations: Recommendation[]
}
