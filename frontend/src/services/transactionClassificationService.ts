import type { TransactionClassificationRequest, TransactionClassificationResponse } from '../types'
import { classifyFinancialTransactionsMock } from '../mocks/transactionClassificationApiMock'
import { isMockApiEnabled } from '../utils/environment'
import { classifyTransactions } from './transactionClassificationApi'

export function classifyFinancialTransactions(request: TransactionClassificationRequest): Promise<TransactionClassificationResponse> {
  return isMockApiEnabled()
    ? classifyFinancialTransactionsMock(request)
    : classifyTransactions(request)
}
