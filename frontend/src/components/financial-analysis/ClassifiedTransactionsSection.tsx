import { TransactionType, type ClassifiedTransaction } from '../../types'
import {
  formatCurrency,
  formatIsoDate,
  formatProbability,
  transactionCategoryLabels,
  transactionTypeLabels,
} from '../../utils/financialAnalysisPresentation'
import { transactionCategoryIcons } from '../../utils/transactionCategoryIcon'

type ClassifiedTransactionsSectionProps = {
  transactions: ClassifiedTransaction[]
}

export function ClassifiedTransactionsSection({ transactions }: ClassifiedTransactionsSectionProps) {
  return (
    <section className="result-section" aria-labelledby="classified-transactions-title">
      <div className="result-section-heading">
        <h2 id="classified-transactions-title">Transações analisadas</h2>
        <p>Dados preservados da resposta e, quando aplicável, a categoria prevista.</p>
      </div>
      {transactions.length > 0 ? (
        <ul className="classified-transactions-list">
          {transactions.map((transaction, index) => (
            <li className="classified-transaction-card" key={`${transaction.description}-${index}`}>
              <h3>{transaction.predictedCategory !== undefined && (() => { const Icon = transactionCategoryIcons[transaction.predictedCategory]; return <span className="category-icon" aria-hidden="true"><Icon size={19} strokeWidth={1.8} /></span> })()}{transaction.description}</h3>
              <dl className="transaction-details">
                {transaction.date !== undefined && <div><dt>Data</dt><dd>{formatIsoDate(transaction.date)}</dd></div>}
                {transaction.amount !== undefined && <div><dt>Valor</dt><dd>{formatCurrency(transaction.amount)}</dd></div>}
                {transaction.type !== undefined && <div><dt>Tipo</dt><dd>{transactionTypeLabels[transaction.type]}</dd></div>}
                {transaction.type === TransactionType.INCOME && <div><dt>Categoria</dt><dd>Não aplicável</dd></div>}
                {transaction.predictedCategory !== undefined && <div><dt>Categoria prevista</dt><dd className="category-value">{(() => { const Icon = transactionCategoryIcons[transaction.predictedCategory]; return <><Icon size={16} strokeWidth={1.8} aria-hidden="true" />{transactionCategoryLabels[transaction.predictedCategory]}</> })()}</dd></div>}
                {transaction.classificationProbability !== undefined && <div><dt>Probabilidade da classificação</dt><dd>{formatProbability(transaction.classificationProbability)}</dd></div>}
              </dl>
            </li>
          ))}
        </ul>
      ) : (
        <p className="section-empty-state">Nenhuma transação analisada está disponível.</p>
      )}
    </section>
  )
}
