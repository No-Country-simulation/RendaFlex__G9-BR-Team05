import {
  SavingFrequency,
  TransactionType,
  type FinancialAnalysisFormState,
  type FinancialAnalysisRequest,
} from '../types'

function parseMoney(value: string, field: string) {
  const parsedValue = Number(value)
  if (Number.isNaN(parsedValue)) throw new Error(`Valor monetário inválido em ${field}.`)
  return parsedValue
}

export function mapFinancialAnalysisFormToRequest(form: FinancialAnalysisFormState): FinancialAnalysisRequest {
  if (!Object.values(SavingFrequency).includes(form.savingFrequency as SavingFrequency)) {
    throw new Error('Frequência de poupança inválida.')
  }

  return {
    incomeHistory: form.incomeHistory.map(({ month, amount }) => ({
      month,
      amount: parseMoney(amount, 'incomeHistory.amount'),
    })),
    monthlyDebtPayments: parseMoney(form.monthlyDebtPayments, 'monthlyDebtPayments'),
    otherFixedMonthlyExpenses: parseMoney(form.otherFixedMonthlyExpenses, 'otherFixedMonthlyExpenses'),
    savingFrequency: form.savingFrequency as SavingFrequency,
    transactions: form.transactions.map(({ description, amount, date, type }) => {
      if (!Object.values(TransactionType).includes(type as TransactionType)) {
        throw new Error('Tipo de transação inválido.')
      }
      return {
        description: description.trim(),
        amount: parseMoney(amount, 'transactions.amount'),
        date,
        type: type as TransactionType,
      }
    }),
  }
}
