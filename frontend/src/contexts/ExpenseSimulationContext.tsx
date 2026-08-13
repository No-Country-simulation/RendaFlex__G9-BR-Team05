import { useState, type ReactNode } from 'react'
import type { ExpenseSimulationViewModel } from '../types'
import { ExpenseSimulationContext, expenseSimulationStorageKey, readStoredExpenseSimulationResult } from './expenseSimulationContextValue'

export function ExpenseSimulationProvider({ children }: { children: ReactNode }) {
  const [result, setResultState] = useState<ExpenseSimulationViewModel | null>(readStoredExpenseSimulationResult)
  const setResult = (nextResult: ExpenseSimulationViewModel) => { setResultState(nextResult); sessionStorage.setItem(expenseSimulationStorageKey, JSON.stringify(nextResult)) }
  const clearResult = () => { setResultState(null); sessionStorage.removeItem(expenseSimulationStorageKey) }
  return <ExpenseSimulationContext value={{ result, setResult, clearResult }}>{children}</ExpenseSimulationContext>
}
