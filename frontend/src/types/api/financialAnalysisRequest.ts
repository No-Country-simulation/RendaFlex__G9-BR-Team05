import type { SavingFrequency } from '../enums/savingFrequency'
import type { IncomeHistoryItem, TransactionInput } from './common'

export type FinancialAnalysisRequest = {
  incomeHistory: IncomeHistoryItem[]
  monthlyDebtPayments: number
  otherFixedMonthlyExpenses: number
  savingFrequency: SavingFrequency
  transactions: TransactionInput[]
}
