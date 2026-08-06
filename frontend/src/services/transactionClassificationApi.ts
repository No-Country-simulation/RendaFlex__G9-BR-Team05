import type { TransactionClassificationRequest, TransactionClassificationResponse } from '../types'
import { requestJson } from './apiClient'

export function classifyTransactions(request: TransactionClassificationRequest): Promise<TransactionClassificationResponse> {
  return requestJson<TransactionClassificationResponse>('/api/v1/transactions/classify', {
    method: 'POST',
    body: request,
  })
}
