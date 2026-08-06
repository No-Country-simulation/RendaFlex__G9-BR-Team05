import { createContext } from 'react'
import {
  FinancialProfile,
  SavingFrequency,
  TransactionCategory,
  TransactionType,
  type FinancialAnalysisRequest,
  type FinancialAnalysisResponse,
} from '../types'

// O resultado fica apenas na sessão para evitar persistência prolongada de informações financeiras no navegador.
export const financialAnalysisRequestStorageKey = 'rendaflex.financialAnalysisRequest'
export const financialAnalysisResultStorageKey = 'rendaflex.financialAnalysisResult'
type FinancialAnalysisContextValue = {
  request: FinancialAnalysisRequest | null
  result: FinancialAnalysisResponse | null
  setAnalysis: (request: FinancialAnalysisRequest, result: FinancialAnalysisResponse) => void
  clearResult: () => void
}
export const FinancialAnalysisContext = createContext<FinancialAnalysisContextValue | null>(null)

function isFinancialAnalysisRequest(value: unknown): value is FinancialAnalysisRequest {
  if (!value || typeof value !== 'object') return false
  const candidate = value as Partial<FinancialAnalysisRequest>
  return Array.isArray(candidate.incomeHistory)
    && candidate.incomeHistory.length >= 3
    && candidate.incomeHistory.length <= 6
    && candidate.incomeHistory.every((item) => typeof item.month === 'string' && typeof item.amount === 'number')
    && typeof candidate.monthlyDebtPayments === 'number'
    && typeof candidate.otherFixedMonthlyExpenses === 'number'
    && Object.values(SavingFrequency).includes(candidate.savingFrequency as SavingFrequency)
    && Array.isArray(candidate.transactions)
    && candidate.transactions.every((transaction) => typeof transaction.description === 'string'
      && typeof transaction.amount === 'number'
      && typeof transaction.date === 'string'
      && Object.values(TransactionType).includes(transaction.type as TransactionType))
}

function isFinancialAnalysisResponse(value: unknown): value is FinancialAnalysisResponse {
  if (!value || typeof value !== 'object') return false
  const candidate = value as Partial<FinancialAnalysisResponse>
  const categorySummary = candidate.categorySummary
  const categoryPercentages = candidate.categoryPercentages
  const validCategories = new Set<string>(Object.values(TransactionCategory))
  const validCategorySummary = Boolean(categorySummary && typeof categorySummary === 'object'
    && Object.entries(categorySummary).every(([category, amount]) => validCategories.has(category) && typeof amount === 'number'))
  const validCategoryPercentages = Boolean(categoryPercentages && typeof categoryPercentages === 'object'
    && Object.entries(categoryPercentages).every(([category, percentage]) => validCategories.has(category) && typeof percentage === 'number'))
  const validClassifiedTransactions = Array.isArray(candidate.classifiedTransactions)
    && candidate.classifiedTransactions.every((transaction) => transaction.predictedCategory === undefined
      || validCategories.has(transaction.predictedCategory))
  return Object.values(FinancialProfile).includes(candidate.financialProfile as FinancialProfile) && typeof candidate.probability === 'number' && candidate.probability >= 0 && candidate.probability <= 1 && Boolean(candidate.metrics && typeof candidate.metrics.averageIncome === 'number') && validClassifiedTransactions && Array.isArray(candidate.recommendations) && validCategorySummary && validCategoryPercentages
}

export function readStoredResult() {
  try { const stored = sessionStorage.getItem(financialAnalysisResultStorageKey); if (!stored) return null; const parsed: unknown = JSON.parse(stored); if (isFinancialAnalysisResponse(parsed)) return parsed } catch { sessionStorage.removeItem(financialAnalysisResultStorageKey); return null }
  sessionStorage.removeItem(financialAnalysisResultStorageKey); return null
}

export function readStoredRequest() {
  try { const stored = sessionStorage.getItem(financialAnalysisRequestStorageKey); if (!stored) return null; const parsed: unknown = JSON.parse(stored); if (isFinancialAnalysisRequest(parsed)) return parsed } catch { sessionStorage.removeItem(financialAnalysisRequestStorageKey); return null }
  sessionStorage.removeItem(financialAnalysisRequestStorageKey); return null
}
