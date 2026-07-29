import { useState, type ReactNode } from 'react'
import type { FinancialAnalysisResponse } from '../types'
import { FinancialAnalysisContext, readStoredResult, storageKey } from './financialAnalysisContextValue'

export function FinancialAnalysisProvider({ children }: { children: ReactNode }) {
  const [result, setResultState] = useState<FinancialAnalysisResponse | null>(readStoredResult)
  const setResult = (nextResult: FinancialAnalysisResponse) => { setResultState(nextResult); sessionStorage.setItem(storageKey, JSON.stringify(nextResult)) }
  const clearResult = () => { setResultState(null); sessionStorage.removeItem(storageKey) }
  return <FinancialAnalysisContext value={{ result, setResult, clearResult }}>{children}</FinancialAnalysisContext>
}
