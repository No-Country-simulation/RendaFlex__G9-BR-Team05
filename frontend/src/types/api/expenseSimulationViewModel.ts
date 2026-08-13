import type { FinancialProfile } from '../enums/financialProfile'
import type { ImpactLevel } from '../enums/impactLevel'
import type { FinancialMetrics, Recommendation } from './common'

export type ExpenseSimulationViewModel = {
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
  financialHealthWorsened: boolean
  impactLevel: ImpactLevel
  monthlyInstallmentAmount: number
  currentMetrics: FinancialMetrics
  projectedMetrics: FinancialMetrics
  recommendations: Recommendation[]
}
