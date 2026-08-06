import { createContext } from 'react'
import { FinancialProfile, ImpactLevel, type ExpenseSimulationViewModel } from '../types'

export const expenseSimulationStorageKey = 'rendaflex.expenseSimulationResult'
type ExpenseSimulationContextValue = { result: ExpenseSimulationViewModel | null; setResult: (result: ExpenseSimulationViewModel) => void; clearResult: () => void }
export const ExpenseSimulationContext = createContext<ExpenseSimulationContextValue | null>(null)

function isExpenseSimulationViewModel(value: unknown): value is ExpenseSimulationViewModel {
  if (!value || typeof value !== 'object') return false
  const candidate = value as Partial<ExpenseSimulationViewModel>
  const validProfile = (profile: unknown) => Object.values(FinancialProfile).includes(profile as FinancialProfile)
  const validProbability = (probability: unknown) => typeof probability === 'number' && probability >= 0 && probability <= 1
  return validProfile(candidate.currentProfile) && validProfile(candidate.projectedProfile)
    && validProbability(candidate.currentProbability) && validProbability(candidate.projectedProbability)
    && typeof candidate.profileChanged === 'boolean'
    && typeof candidate.financialHealthWorsened === 'boolean'
    && Object.values(ImpactLevel).includes(candidate.impactLevel as ImpactLevel)
    && typeof candidate.monthlyInstallmentAmount === 'number'
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
    if (isExpenseSimulationViewModel(parsed)) return parsed
  } catch { sessionStorage.removeItem(expenseSimulationStorageKey); return null }
  sessionStorage.removeItem(expenseSimulationStorageKey)
  return null
}
