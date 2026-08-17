import { TransactionCategory, type CategorySummary } from '../../types'
import { formatCurrency, transactionCategoryLabels } from '../../utils/financialAnalysisPresentation'
import { transactionCategoryIcons } from '../../utils/transactionCategoryIcon'

type CategorySummarySectionProps = {
  categorySummary: CategorySummary
}

export function CategorySummarySection({ categorySummary }: CategorySummarySectionProps) {
  const categories = Object.values(TransactionCategory).flatMap((category) => {
    const amount = categorySummary[category]
    return amount === undefined ? [] : [{ category, amount }]
  })

  return (
    <section className="result-section" aria-labelledby="category-summary-title">
      <div className="result-section-heading">
        <h2 id="category-summary-title">Resumo de gastos por categoria</h2>
        <p>Valores de despesas categorizadas retornados pela análise.</p>
      </div>
      {categories.length > 0 ? (
        <dl className="category-summary-grid">
          {categories.map(({ category, amount }) => (
            <div className="category-summary-item" key={category}>
              <dt><span className="category-icon" aria-hidden="true">{(() => { const Icon = transactionCategoryIcons[category]; return <Icon size={18} strokeWidth={1.8} /> })()}</span>{transactionCategoryLabels[category]}</dt>
              <dd>{formatCurrency(amount)}</dd>
            </div>
          ))}
        </dl>
      ) : (
        <p className="section-empty-state">Nenhum resumo de gastos por categoria está disponível.</p>
      )}
    </section>
  )
}
