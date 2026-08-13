import { useContext } from 'react'
import { FinancialAnalysisContext } from '../contexts/financialAnalysisContextValue'

export function useFinancialAnalysis() {
  const context = useContext(FinancialAnalysisContext)
  if (!context) throw new Error('useFinancialAnalysis deve ser usado dentro de FinancialAnalysisProvider.')
  return context
}
