import { financialAnalysisResponseMock } from '../../mocks/financialAnalysisResponseMock'
import type { FinancialAnalysisResponse } from '../../types'

export function analyzeFinancialSituation(): Promise<FinancialAnalysisResponse> {
  // O mock representa a resposta pública futura do Spring Boot; nenhuma métrica financeira é calculada no frontend.
  return new Promise((resolve) => window.setTimeout(() => resolve(structuredClone(financialAnalysisResponseMock)), 700))
}
