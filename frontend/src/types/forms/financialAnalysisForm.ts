import type { SavingFrequency } from '../enums/savingFrequency'

export type IncomeHistoryFormItem = {
  id: string
  month: string
  amount: string
}

export type FinancialAnalysisFormState = {
  incomeHistory: IncomeHistoryFormItem[]
  savingFrequency: SavingFrequency | ''
}
