import { createContext } from 'react'
import { FinancialProfile, type FinancialAnalysisResponse } from '../types'

// O resultado fica apenas na sessão para evitar persistência prolongada de informações financeiras no navegador.
export const storageKey = 'rendaflex.financialAnalysisResult'
type FinancialAnalysisContextValue = { result: FinancialAnalysisResponse | null; setResult: (result: FinancialAnalysisResponse) => void; clearResult: () => void }
export const FinancialAnalysisContext = createContext<FinancialAnalysisContextValue | null>(null)

function isFinancialAnalysisResponse(value: unknown): value is FinancialAnalysisResponse {
  if (!value || typeof value !== 'object') return false
  const candidate = value as Partial<FinancialAnalysisResponse>
  return Object.values(FinancialProfile).includes(candidate.financialProfile as FinancialProfile) && typeof candidate.probability === 'number' && candidate.probability >= 0 && candidate.probability <= 1 && Boolean(candidate.metrics && typeof candidate.metrics.averageIncome === 'number') && Array.isArray(candidate.classifiedTransactions) && Array.isArray(candidate.recommendations) && Boolean(candidate.categorySummary && typeof candidate.categorySummary === 'object')
}

export function readStoredResult() {
  try { const stored = sessionStorage.getItem(storageKey); if (!stored) return null; const parsed: unknown = JSON.parse(stored); if (isFinancialAnalysisResponse(parsed)) return parsed } catch { sessionStorage.removeItem(storageKey); return null }
  sessionStorage.removeItem(storageKey); return null
}
