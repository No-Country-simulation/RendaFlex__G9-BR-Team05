import {
  FinancialProfile,
  ImpactLevel,
  RecommendationPriority,
  type ExpenseSimulationRequest,
  type ExpenseSimulationResponse,
} from '../types'

export function createExpenseSimulationMock(request: ExpenseSimulationRequest): Promise<ExpenseSimulationResponse> {
  // No modo real, o installmentAmount é calculado e fornecido pela API.
  const installmentAmount = Math.round((request.newExpense.totalAmount / request.newExpense.installmentCount + Number.EPSILON) * 100) / 100
  const response: ExpenseSimulationResponse = {
    newExpense: { ...request.newExpense, installmentAmount },
    currentScenario: {
      financialProfile: FinancialProfile.HEALTHY,
      probability: 0.84,
      metrics: { averageIncome: 4200, incomeVariationCoefficientPercentage: 11.5, debtRatioPercentage: 22, fixedCommitmentPercentage: 34 },
    },
    projectedScenario: {
      financialProfile: FinancialProfile.UNDER_OBSERVATION,
      probability: 0.71,
      metrics: { averageIncome: 4200, incomeVariationCoefficientPercentage: 11.5, debtRatioPercentage: 22, fixedCommitmentPercentage: 39.95 },
    },
    profileChanged: true,
    financialHealthWorsened: true,
    impactLevel: ImpactLevel.HIGH,
    recommendations: [
      { priority: RecommendationPriority.HIGH, message: 'A nova despesa aumenta significativamente o comprometimento mensal.' },
      { priority: RecommendationPriority.MEDIUM, message: 'Considere reduzir o número de parcelas ou adiar a compra.' },
    ],
  }

  return new Promise((resolve) => window.setTimeout(() => resolve(response), 700))
}
