import type { FinancialAnalysisRequest, FinancialAnalysisResponse } from '../types'
import { financialAnalysisResponseMock } from './financialAnalysisResponseMock'

export function createFinancialAnalysisMock(request: FinancialAnalysisRequest): Promise<FinancialAnalysisResponse> {
  void request
  return new Promise((resolve) => window.setTimeout(
    () => resolve(structuredClone(financialAnalysisResponseMock)),
    700,
  ))
}
