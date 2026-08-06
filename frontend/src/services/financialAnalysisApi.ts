import type { FinancialAnalysisRequest, FinancialAnalysisResponse } from '../types'
import { requestJson } from './apiClient'

export function createFinancialAnalysis(request: FinancialAnalysisRequest): Promise<FinancialAnalysisResponse> {
  return requestJson<FinancialAnalysisResponse>('/api/v1/financial-analyses', {
    method: 'POST',
    body: request,
  })
}
