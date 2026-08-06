import {
  TransactionCategory,
  TransactionType,
  type ClassifiedTransaction,
  type TransactionClassificationRequest,
  type TransactionClassificationResponse,
} from '../types'

export function classifyFinancialTransactionsMock(request: TransactionClassificationRequest): Promise<TransactionClassificationResponse> {
  const transactions: ClassifiedTransaction[] = request.transactions.map((transaction) => {
    if (transaction.type === TransactionType.INCOME) return { ...transaction }
    return { ...transaction, predictedCategory: TransactionCategory.OTHER, classificationProbability: 1 }
  })
  const classifiedTotal = transactions.reduce((total, transaction) => {
    if (transaction.predictedCategory !== TransactionCategory.OTHER || transaction.amount === undefined) return total
    return total + transaction.amount
  }, 0)
  const response: TransactionClassificationResponse = {
    transactions,
    categorySummary: classifiedTotal > 0 ? { [TransactionCategory.OTHER]: classifiedTotal } : {},
    categoryPercentages: classifiedTotal > 0 ? { [TransactionCategory.OTHER]: 100 } : {},
  }

  return Promise.resolve(response)
}
