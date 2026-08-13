import type { IncomeHistoryErrors, IncomeHistoryFormItem } from '../../types'

type Props = { item: IncomeHistoryFormItem; index: number; canRemove: boolean; errors?: IncomeHistoryErrors[string]; onChange: (id: string, field: 'month' | 'amount', value: string) => void; onRemove: (id: string) => void }

export function IncomeHistoryItemFields({ item, index, canRemove, errors = {}, onChange, onRemove }: Props) {
  const monthErrorId = `income-month-${item.id}-error`; const amountErrorId = `income-amount-${item.id}-error`
  const description = item.month ? `o mês ${item.month}` : `o registro ${index + 1}`
  return <fieldset className="income-row">
    <legend>Mês {index + 1}</legend>
    <div className="field-group"><label htmlFor={`income-month-${item.id}`}>Mês</label><input id={`income-month-${item.id}`} type="month" value={item.month} aria-invalid={Boolean(errors.month)} aria-describedby={errors.month ? monthErrorId : undefined} onChange={(event) => onChange(item.id, 'month', event.target.value)} />{errors.month && <span className="field-error" id={monthErrorId}>{errors.month}</span>}</div>
    <div className="field-group"><label htmlFor={`income-amount-${item.id}`}>Valor recebido</label><div className="money-input"><span aria-hidden="true">R$</span><input id={`income-amount-${item.id}`} type="number" min="0" step="0.01" inputMode="decimal" placeholder="0,00" value={item.amount} aria-invalid={Boolean(errors.amount)} aria-describedby={errors.amount ? amountErrorId : undefined} onChange={(event) => onChange(item.id, 'amount', event.target.value)} /></div>{errors.amount && <span className="field-error" id={amountErrorId}>{errors.amount}</span>}</div>
    <button className="remove-button" type="button" disabled={!canRemove} onClick={() => onRemove(item.id)} aria-label={`Remover ${description}`}>Remover</button>
  </fieldset>
}
