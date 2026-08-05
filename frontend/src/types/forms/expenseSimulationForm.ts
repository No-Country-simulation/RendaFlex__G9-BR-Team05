export type ExpenseSimulationFormState = {
  expenseDescription: string
  totalAmount: string
  installments: string
}

export type ExpenseSimulationFormErrors = {
  expenseDescription?: string
  totalAmount?: string
  installments?: string
}
