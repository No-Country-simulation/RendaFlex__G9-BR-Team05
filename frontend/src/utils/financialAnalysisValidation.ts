import { SavingFrequency, TransactionType } from '../types'
import type { FinancialAnalysisFormErrors, FinancialAnalysisFormState } from '../types'

const validMonthPattern = /^\d{4}-(0[1-9]|1[0-2])$/
const validDatePattern = /^\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01])$/

function isValidDate(value: string) {
  if (!validDatePattern.test(value)) return false
  const [year, month, day] = value.split('-').map(Number)
  const date = new Date(Date.UTC(year, month - 1, day))
  return date.getUTCFullYear() === year && date.getUTCMonth() === month - 1 && date.getUTCDate() === day
}

export const getLatestIncomeMonth = (state: FinancialAnalysisFormState) =>
  state.incomeHistory.map((item) => item.month).filter((month) => validMonthPattern.test(month)).sort().at(-1) ?? ''

export function validateFinancialAnalysisForm(state: FinancialAnalysisFormState): FinancialAnalysisFormErrors {
  const incomeHistory: FinancialAnalysisFormErrors['incomeHistory'] = {}
  const transactions: FinancialAnalysisFormErrors['transactions'] = {}
  const monthCounts = new Map<string, number>()
  state.incomeHistory.forEach((item) => monthCounts.set(item.month, (monthCounts.get(item.month) ?? 0) + 1))

  state.incomeHistory.forEach((item) => {
    const errors: (typeof incomeHistory)[string] = {}
    if (!item.month) errors.month = 'Informe o mês da renda.'
    else if (!validMonthPattern.test(item.month)) errors.month = 'Informe um mês válido.'
    else if ((monthCounts.get(item.month) ?? 0) > 1) errors.month = 'Este mês já foi informado.'
    if (item.amount === '') errors.amount = 'Informe o valor recebido.'
    else if (!Number.isFinite(Number(item.amount)) || Number(item.amount) < 0) errors.amount = 'O valor deve ser maior ou igual a zero.'
    if (Object.keys(errors).length) incomeHistory[item.id] = errors
  })

  const latestMonth = getLatestIncomeMonth(state)
  state.transactions.forEach((item) => {
    const errors: (typeof transactions)[string] = {}
    if (!item.description.trim()) errors.description = 'Informe uma descrição.'
    if (item.amount === '') errors.amount = 'Informe o valor da transação.'
    else if (!Number.isFinite(Number(item.amount)) || Number(item.amount) <= 0) errors.amount = 'O valor deve ser maior que zero.'
    if (!item.date) errors.date = 'Informe a data da transação.'
    else if (!isValidDate(item.date)) errors.date = 'Informe uma data válida.'
    else if (!latestMonth || item.date.slice(0, 7) !== latestMonth) errors.date = latestMonth ? `A data deve pertencer ao mês ${latestMonth}.` : 'Preencha um mês válido no histórico de renda.'
    if (!Object.values(TransactionType).includes(item.type as TransactionType)) errors.type = 'Selecione receita ou despesa.'
    if (Object.keys(errors).length) transactions[item.id] = errors
  })

  const savingFrequency = Object.values(SavingFrequency).includes(state.savingFrequency as SavingFrequency)
    ? undefined
    : 'Selecione a frequência de poupança.'
  return { incomeHistory, savingFrequency, transactions }
}

export const hasFormErrors = (errors: FinancialAnalysisFormErrors) =>
  Boolean(Object.keys(errors.incomeHistory).length || errors.savingFrequency || Object.keys(errors.transactions).length)
