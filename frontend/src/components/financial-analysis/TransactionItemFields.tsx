import { TransactionType, type TransactionErrors, type TransactionFormItem } from '../../types'

type Props = { item: TransactionFormItem; index: number; canRemove: boolean; errors?: TransactionErrors[string]; onChange: (id: string, field: 'description' | 'amount' | 'date' | 'type', value: string) => void; onRemove: (id: string) => void }

export function TransactionItemFields({ item, index, canRemove, errors = {}, onChange, onRemove }: Props) {
  const field = (name: keyof typeof errors) => ({
    id: `transaction-${name}-${item.id}`,
    errorId: `transaction-${name}-${item.id}-error`,
    error: errors[name],
  })
  const description = field('description'); const amount = field('amount'); const date = field('date'); const type = field('type')
  return (
    <fieldset className="transaction-row">
      <legend>Transação {index + 1}</legend>
      <div className="field-group"><label htmlFor={description.id}>Descrição</label><input id={description.id} value={item.description} aria-invalid={Boolean(description.error)} aria-describedby={description.error ? description.errorId : undefined} onChange={(event) => onChange(item.id, 'description', event.target.value)} />{description.error && <span className="field-error" id={description.errorId}>{description.error}</span>}</div>
      <div className="field-group"><label htmlFor={amount.id}>Valor</label><div className="money-input"><span aria-hidden="true">R$</span><input id={amount.id} type="number" step="0.01" inputMode="decimal" value={item.amount} aria-invalid={Boolean(amount.error)} aria-describedby={amount.error ? amount.errorId : undefined} onChange={(event) => onChange(item.id, 'amount', event.target.value)} /></div>{amount.error && <span className="field-error" id={amount.errorId}>{amount.error}</span>}</div>
      <div className="field-group"><label htmlFor={date.id}>Data</label><input id={date.id} type="date" value={item.date} aria-invalid={Boolean(date.error)} aria-describedby={date.error ? date.errorId : undefined} onChange={(event) => onChange(item.id, 'date', event.target.value)} />{date.error && <span className="field-error" id={date.errorId}>{date.error}</span>}</div>
      <div className="field-group"><label htmlFor={type.id}>Tipo</label><select id={type.id} value={item.type} aria-invalid={Boolean(type.error)} aria-describedby={type.error ? type.errorId : undefined} onChange={(event) => onChange(item.id, 'type', event.target.value)}><option value="">Selecione</option><option value={TransactionType.INCOME}>Receita</option><option value={TransactionType.EXPENSE}>Despesa</option></select>{type.error && <span className="field-error" id={type.errorId}>{type.error}</span>}</div>
      <button className="remove-button" type="button" disabled={!canRemove} onClick={() => onRemove(item.id)} aria-label={`Remover transação ${index + 1}`}>Remover</button>
    </fieldset>
  )
}
