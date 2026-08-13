import {
  FinancialProfile,
  RecommendationPriority,
  TransactionCategory,
  TransactionType,
  type FinancialProfile as FinancialProfileValue,
  type RecommendationPriority as RecommendationPriorityValue,
  type TransactionCategory as TransactionCategoryValue,
  type TransactionType as TransactionTypeValue,
} from '../types'

export const financialProfileDetails: Record<FinancialProfileValue, { name: string; description: string }> = {
  [FinancialProfile.HEALTHY]: {
    name: 'Saudável',
    description: 'Seus indicadores mostram uma situação financeira equilibrada.',
  },
  [FinancialProfile.UNDER_OBSERVATION]: {
    name: 'Em observação',
    description: 'Alguns indicadores merecem atenção para preservar sua flexibilidade financeira.',
  },
  [FinancialProfile.AT_RISK]: {
    name: 'Em risco',
    description: 'Seus indicadores apontam a necessidade de rever compromissos financeiros.',
  },
}

export const transactionCategoryLabels: Record<TransactionCategoryValue, string> = {
  [TransactionCategory.FOOD]: 'Alimentação',
  [TransactionCategory.TRANSPORT]: 'Transporte',
  [TransactionCategory.HOUSING]: 'Moradia',
  [TransactionCategory.HEALTH]: 'Saúde',
  [TransactionCategory.EDUCATION]: 'Educação',
  [TransactionCategory.ENTERTAINMENT]: 'Lazer',
  [TransactionCategory.SERVICES]: 'Serviços',
  [TransactionCategory.OTHER]: 'Outras',
}

export const transactionTypeLabels: Record<TransactionTypeValue, string> = {
  [TransactionType.INCOME]: 'Receita',
  [TransactionType.EXPENSE]: 'Despesa',
}

export const recommendationPriorityLabels: Record<RecommendationPriorityValue, string> = {
  [RecommendationPriority.LOW]: 'Baixa',
  [RecommendationPriority.MEDIUM]: 'Média',
  [RecommendationPriority.HIGH]: 'Alta',
}

export const formatCurrency = new Intl.NumberFormat('pt-BR', {
  style: 'currency',
  currency: 'BRL',
}).format

const percentageFormatter = new Intl.NumberFormat('pt-BR', { maximumFractionDigits: 2 })

export function formatPercentage(value: number) {
  return `${percentageFormatter.format(value)}%`
}

export function formatProbability(value: number) {
  return formatPercentage(value * 100)
}

export function formatIsoDate(value: string) {
  const match = /^(\d{4})-(\d{2})-(\d{2})$/.exec(value)
  if (!match) return value

  const [, year, month, day] = match
  const parsedDate = new Date(Number(year), Number(month) - 1, Number(day))
  const isValidDate = parsedDate.getFullYear() === Number(year)
    && parsedDate.getMonth() === Number(month) - 1
    && parsedDate.getDate() === Number(day)

  return isValidDate ? `${day}/${month}/${year}` : value
}
