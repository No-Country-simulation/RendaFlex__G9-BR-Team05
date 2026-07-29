import type { SavingFrequency } from '../enums/savingFrequency'
import type { TransactionType } from '../enums/transactionType'

export type IncomeHistoryFormItem = {
  id: string
  month: string
  amount: string
}

export type FinancialAnalysisFormState = {
  incomeHistory: IncomeHistoryFormItem[]
  savingFrequency: SavingFrequency | ''
  transactions: TransactionFormItem[]
}

export type TransactionFormItem = {
  id: string
  description: string
  amount: string
  date: string
  type: TransactionType | ''
}

export type IncomeHistoryErrors = Record<string, Partial<Record<'month' | 'amount', string>>>
export type TransactionErrors = Record<string, Partial<Record<'description' | 'amount' | 'date' | 'type', string>>>

export type FinancialAnalysisFormErrors = {
  incomeHistory: IncomeHistoryErrors
  savingFrequency?: string
  transactions: TransactionErrors
}
