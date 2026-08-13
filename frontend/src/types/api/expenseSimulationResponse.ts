import type { FinancialProfile } from '../enums/financialProfile'
import type { ImpactLevel } from '../enums/impactLevel'
import type { FinancialMetrics, Recommendation } from './common'

export type SimulatedExpenseResult = {
  description: string
  totalAmount: number
  installmentCount: number
  installmentAmount: number
}

export type FinancialScenario = {
  financialProfile: FinancialProfile
  probability: number
  metrics: FinancialMetrics
}

export type ExpenseSimulationResponse = {
  newExpense: SimulatedExpenseResult
  currentScenario: FinancialScenario
  projectedScenario: FinancialScenario
  profileChanged: boolean
  financialHealthWorsened: boolean
  impactLevel: ImpactLevel
  recommendations: Recommendation[]
}
