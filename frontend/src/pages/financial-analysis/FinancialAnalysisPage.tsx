import { useRef, useState } from 'react'
import { ButtonLink } from '../../components/common/ButtonLink'
import { IncomeHistorySection } from '../../components/financial-analysis/IncomeHistorySection'
import { MonthlyCommitmentsSection } from '../../components/financial-analysis/MonthlyCommitmentsSection'
import { SavingFrequencyField } from '../../components/financial-analysis/SavingFrequencyField'
import type { FinancialAnalysisFormState } from '../../types'

const createInitialIncomeHistory = () => [1, 2, 3].map((id) => ({ id: String(id), month: '', amount: '' }))

export function FinancialAnalysisPage() {
  const nextId = useRef(4)
  const [formState, setFormState] = useState<FinancialAnalysisFormState>({ incomeHistory: createInitialIncomeHistory(), savingFrequency: '' })

  const addIncomeMonth = () => {
    if (formState.incomeHistory.length >= 6) return
    const id = String(nextId.current++)
    setFormState((current) => ({ ...current, incomeHistory: [...current.incomeHistory, { id, month: '', amount: '' }] }))
  }

  const removeIncomeMonth = (id: string) => {
    setFormState((current) => current.incomeHistory.length <= 3 ? current : { ...current, incomeHistory: current.incomeHistory.filter((item) => item.id !== id) })
  }

  const updateIncomeMonth = (id: string, field: 'month' | 'amount', value: string) => {
    setFormState((current) => ({ ...current, incomeHistory: current.incomeHistory.map((item) => item.id === id ? { ...item, [field]: value } : item) }))
  }

  return (
    <div className="analysis-page">
      <header className="page-heading"><span className="eyebrow">Análise financeira</span><h1>Vamos conhecer melhor a sua renda</h1><p>Preencha as informações disponíveis. Nesta etapa do protótipo, seus dados permanecem somente nesta página e ainda não serão enviados.</p></header>
      <form className="analysis-form" onSubmit={(event) => event.preventDefault()}>
        <IncomeHistorySection items={formState.incomeHistory} onAdd={addIncomeMonth} onChange={updateIncomeMonth} onRemove={removeIncomeMonth} />
        <SavingFrequencyField value={formState.savingFrequency} onChange={(savingFrequency) => setFormState((current) => ({ ...current, savingFrequency }))} />
        <MonthlyCommitmentsSection />
        <div className="form-footer"><p>O envio da análise será habilitado em uma próxima etapa.</p><div><ButtonLink to="/" variant="secondary">Voltar ao início</ButtonLink><button className="button button-primary" type="submit" disabled>Continuar análise</button></div></div>
      </form>
    </div>
  )
}
