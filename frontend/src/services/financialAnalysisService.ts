import type { FinancialAnalysisRequest, FinancialAnalysisResponse } from '../types'
import { createFinancialAnalysisMock } from '../mocks/financialAnalysisApiMock'
import { isMockApiEnabled } from '../utils/environment'
import { createFinancialAnalysis } from './financialAnalysisApi'

export function analyzeFinancialSituation(request: FinancialAnalysisRequest): Promise<FinancialAnalysisResponse> {
  return isMockApiEnabled()
    ? createFinancialAnalysisMock(request)
    : createFinancialAnalysis(request)
}
