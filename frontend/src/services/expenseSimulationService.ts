import type { ExpenseSimulationRequest, ExpenseSimulationResponse } from '../types'
import { createExpenseSimulationMock } from '../mocks/expenseSimulationApiMock'
import { isMockApiEnabled } from '../utils/environment'
import { createExpenseSimulation } from './expenseSimulationApi'

export function simulateExpense(request: ExpenseSimulationRequest): Promise<ExpenseSimulationResponse> {
  return isMockApiEnabled()
    ? createExpenseSimulationMock(request)
    : createExpenseSimulation(request)
}
