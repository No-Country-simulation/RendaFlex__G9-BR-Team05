import { useContext } from 'react'
import { ExpenseSimulationContext } from '../contexts/expenseSimulationContextValue'

export function useExpenseSimulation() {
  const context = useContext(ExpenseSimulationContext)
  if (!context) throw new Error('useExpenseSimulation deve ser usado dentro de ExpenseSimulationProvider.')
  return context
}
