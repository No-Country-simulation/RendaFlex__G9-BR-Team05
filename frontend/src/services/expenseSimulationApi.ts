import type { ExpenseSimulationRequest, ExpenseSimulationResponse } from '../types'
import { requestJson } from './apiClient'

export function createExpenseSimulation(request: ExpenseSimulationRequest): Promise<ExpenseSimulationResponse> {
  return requestJson<ExpenseSimulationResponse>('/api/v1/expense-simulations', {
    method: 'POST',
    body: request,
  })
}
