import { createContext } from 'react'
import { FinancialProfile, type ExpenseSimulationResponse } from '../types'

export const expenseSimulationStorageKey = 'rendaflex.expenseSimulationResult'
type ExpenseSimulationContextValue = { result: ExpenseSimulationResponse | null; setResult: (result: ExpenseSimulationResponse) => void; clearResult: () => void }
export const ExpenseSimulationContext = createContext<ExpenseSimulationContextValue | null>(null)

function isExpenseSimulationResponse(value: unknown): value is ExpenseSimulationResponse {
  if (!value || typeof value !== 'object') return false
  const candidate = value as Partial<ExpenseSimulationResponse>
  const validProfile = (profile: unknown) => Object.values(FinancialProfile).includes(profile as FinancialProfile)
  const validProbability = (probability: unknown) => typeof probability === 'number' && probability >= 0 && probability <= 1
  return validProfile(candidate.currentProfile) && validProfile(candidate.projectedProfile)
    && validProbability(candidate.currentProbability) && validProbability(candidate.projectedProbability)
    && typeof candidate.profileChanged === 'boolean' && typeof candidate.monthlyInstallmentAmount === 'number'
    && Boolean(candidate.simulatedExpense && typeof candidate.simulatedExpense.description === 'string' && typeof candidate.simulatedExpense.totalAmount === 'number' && typeof candidate.simulatedExpense.installments === 'number' && typeof candidate.simulatedExpense.monthlyAmount === 'number')
    && Boolean(candidate.currentMetrics && typeof candidate.currentMetrics.averageIncome === 'number')
    && Boolean(candidate.projectedMetrics && typeof candidate.projectedMetrics.averageIncome === 'number')
    && Array.isArray(candidate.recommendations)
}

export function readStoredExpenseSimulationResult() {
  try {
    const stored = sessionStorage.getItem(expenseSimulationStorageKey)
    if (!stored) return null
    const parsed: unknown = JSON.parse(stored)
    if (isExpenseSimulationResponse(parsed)) return parsed
  } catch { sessionStorage.removeItem(expenseSimulationStorageKey); return null }
  sessionStorage.removeItem(expenseSimulationStorageKey)
  return null
}
