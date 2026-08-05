import { FinancialProfile, RecommendationPriority, type ExpenseSimulationResponse } from '../types'

export const expenseSimulationResponseMock: ExpenseSimulationResponse = {
  simulatedExpense: { description: 'Notebook para trabalho', totalAmount: 3000, installments: 12, monthlyAmount: 250 },
  currentProfile: FinancialProfile.HEALTHY,
  projectedProfile: FinancialProfile.UNDER_OBSERVATION,
  currentProbability: 0.84,
  projectedProbability: 0.71,
  profileChanged: true,
  monthlyInstallmentAmount: 250,
  currentMetrics: { averageIncome: 4200, incomeVariationCoefficientPercentage: 11.5, debtRatioPercentage: 22, fixedCommitmentPercentage: 34 },
  projectedMetrics: { averageIncome: 4200, incomeVariationCoefficientPercentage: 11.5, debtRatioPercentage: 22, fixedCommitmentPercentage: 39.95 },
  recommendations: [
    { priority: RecommendationPriority.HIGH, message: 'A nova despesa aumenta significativamente o comprometimento mensal.' },
    { priority: RecommendationPriority.MEDIUM, message: 'Considere reduzir o número de parcelas ou adiar a compra.' },
  ],
}
