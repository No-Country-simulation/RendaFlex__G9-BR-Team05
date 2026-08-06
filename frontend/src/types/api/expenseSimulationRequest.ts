import type { FinancialAnalysisRequest } from './financialAnalysisRequest'

export type NewExpenseInput = {
  description: string
  totalAmount: number
  installmentCount: number
}

export type ExpenseSimulationRequest = FinancialAnalysisRequest & {
  newExpense: NewExpenseInput
}
