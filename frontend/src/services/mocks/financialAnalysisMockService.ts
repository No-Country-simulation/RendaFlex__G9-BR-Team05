import { financialAnalysisResponseMock } from '../../mocks/financialAnalysisResponseMock'
import type { FinancialAnalysisFormState, FinancialAnalysisResponse } from '../../types'

export function analyzeFinancialSituation(form: FinancialAnalysisFormState): Promise<FinancialAnalysisResponse> {
  void form
  // O mock representa a resposta pública futura do Spring Boot; nenhuma métrica financeira é calculada no frontend.
  return new Promise((resolve) => window.setTimeout(() => resolve(structuredClone(financialAnalysisResponseMock)), 700))
}
