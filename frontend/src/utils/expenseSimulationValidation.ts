import type { ExpenseSimulationFormErrors, ExpenseSimulationFormState } from '../types'

export function validateExpenseSimulationForm(state: ExpenseSimulationFormState): ExpenseSimulationFormErrors {
  const errors: ExpenseSimulationFormErrors = {}
  if (!state.expenseDescription.trim()) errors.expenseDescription = 'Informe uma descrição.'
  if (state.totalAmount === '') errors.totalAmount = 'Informe o valor total.'
  else if (!Number.isFinite(Number(state.totalAmount)) || Number(state.totalAmount) <= 0) errors.totalAmount = 'O valor deve ser maior que zero.'
  if (state.installments === '') errors.installments = 'Informe a quantidade de parcelas.'
  else if (!Number.isInteger(Number(state.installments)) || Number(state.installments) < 1 || Number(state.installments) > 72) errors.installments = 'Informe um número inteiro entre 1 e 72.'
  return errors
}

export const hasExpenseSimulationFormErrors = (errors: ExpenseSimulationFormErrors) => Boolean(errors.expenseDescription || errors.totalAmount || errors.installments)
