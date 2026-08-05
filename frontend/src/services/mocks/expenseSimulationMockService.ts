import { expenseSimulationResponseMock } from '../../mocks/expenseSimulationResponseMock'
import type { ExpenseSimulationFormState, ExpenseSimulationResponse } from '../../types'
import type { FinancialAnalysisResponse } from '../../types'

export function simulateExpense(analysis: FinancialAnalysisResponse, expense: ExpenseSimulationFormState): Promise<ExpenseSimulationResponse> {
  void analysis
  const monthlyAmount = Number(expense.totalAmount) / Number(expense.installments)
  const response = structuredClone(expenseSimulationResponseMock)
  response.simulatedExpense = { description: expense.expenseDescription.trim(), totalAmount: Number(expense.totalAmount), installments: Number(expense.installments), monthlyAmount }
  response.monthlyInstallmentAmount = monthlyAmount
  return new Promise((resolve) => window.setTimeout(() => resolve(response), 700))
}
