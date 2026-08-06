import { useState, type ReactNode } from 'react'
import type { FinancialAnalysisRequest, FinancialAnalysisResponse } from '../types'
import { expenseSimulationStorageKey } from './expenseSimulationContextValue'
import { FinancialAnalysisContext, financialAnalysisRequestStorageKey, financialAnalysisResultStorageKey, readStoredRequest, readStoredResult } from './financialAnalysisContextValue'

export function FinancialAnalysisProvider({ children }: { children: ReactNode }) {
  const [request, setRequestState] = useState<FinancialAnalysisRequest | null>(readStoredRequest)
  const [result, setResultState] = useState<FinancialAnalysisResponse | null>(readStoredResult)
  const setAnalysis = (nextRequest: FinancialAnalysisRequest, nextResult: FinancialAnalysisResponse) => {
    setRequestState(nextRequest)
    setResultState(nextResult)
    sessionStorage.setItem(financialAnalysisRequestStorageKey, JSON.stringify(nextRequest))
    sessionStorage.setItem(financialAnalysisResultStorageKey, JSON.stringify(nextResult))
    sessionStorage.removeItem(expenseSimulationStorageKey)
  }
  const clearResult = () => {
    setRequestState(null)
    setResultState(null)
    sessionStorage.removeItem(financialAnalysisRequestStorageKey)
    sessionStorage.removeItem(financialAnalysisResultStorageKey)
  }
  return <FinancialAnalysisContext value={{ request, result, setAnalysis, clearResult }}>{children}</FinancialAnalysisContext>
}
