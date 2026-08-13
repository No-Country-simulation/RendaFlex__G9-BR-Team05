import type {
  ExpenseSimulationFormState,
  ExpenseSimulationRequest,
  ExpenseSimulationResponse,
  ExpenseSimulationViewModel,
  FinancialAnalysisRequest,
} from '../types'

export function mapExpenseSimulationFormToRequest(
  financialContext: FinancialAnalysisRequest,
  form: ExpenseSimulationFormState,
): ExpenseSimulationRequest {
  const description = form.expenseDescription.trim()
  const totalAmount = Number(form.totalAmount)
  const installmentCount = Number(form.installments)

  if (!description) throw new Error('A descrição da nova despesa é obrigatória.')
  if (Number.isNaN(totalAmount)) throw new Error('O valor total da nova despesa é inválido.')
  if (totalAmount <= 0) throw new Error('O valor total da nova despesa deve ser maior que zero.')
  if (Number.isNaN(installmentCount)) throw new Error('A quantidade de parcelas é inválida.')
  if (!Number.isInteger(installmentCount)) throw new Error('A quantidade de parcelas deve ser um número inteiro.')
  if (installmentCount < 1) throw new Error('A quantidade de parcelas deve ser maior ou igual a um.')

  return {
    ...financialContext,
    incomeHistory: financialContext.incomeHistory.map((item) => ({ ...item })),
    transactions: financialContext.transactions.map((transaction) => ({ ...transaction })),
    newExpense: { description, totalAmount, installmentCount },
  }
}

export function mapExpenseSimulationResponseToViewModel(
  response: ExpenseSimulationResponse,
): ExpenseSimulationViewModel {
  return {
    simulatedExpense: {
      description: response.newExpense.description,
      totalAmount: response.newExpense.totalAmount,
      installments: response.newExpense.installmentCount,
      monthlyAmount: response.newExpense.installmentAmount,
    },
    currentProfile: response.currentScenario.financialProfile,
    projectedProfile: response.projectedScenario.financialProfile,
    currentProbability: response.currentScenario.probability,
    projectedProbability: response.projectedScenario.probability,
    profileChanged: response.profileChanged,
    financialHealthWorsened: response.financialHealthWorsened,
    impactLevel: response.impactLevel,
    monthlyInstallmentAmount: response.newExpense.installmentAmount,
    currentMetrics: { ...response.currentScenario.metrics },
    projectedMetrics: { ...response.projectedScenario.metrics },
    recommendations: response.recommendations.map((recommendation) => ({ ...recommendation })),
  }
}
