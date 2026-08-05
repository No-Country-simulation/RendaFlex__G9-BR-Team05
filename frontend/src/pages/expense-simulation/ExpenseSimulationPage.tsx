import { useState, type FormEvent } from 'react'
import { useNavigate } from 'react-router-dom'
import { ButtonLink } from '../../components/common/ButtonLink'
import { NewExpenseSection } from '../../components/expense-simulation/NewExpenseSection'
import { useExpenseSimulation } from '../../hooks/useExpenseSimulation'
import { useFinancialAnalysis } from '../../hooks/useFinancialAnalysis'
import { simulateExpense } from '../../services/mocks/expenseSimulationMockService'
import type { ExpenseSimulationFormErrors, ExpenseSimulationFormState } from '../../types'
import { financialProfileDetails, formatCurrency, formatPercentage, formatProbability } from '../../utils/financialAnalysisPresentation'
import { hasExpenseSimulationFormErrors, validateExpenseSimulationForm } from '../../utils/expenseSimulationValidation'

const initialState: ExpenseSimulationFormState = { expenseDescription: '', totalAmount: '', installments: '1' }

export function ExpenseSimulationPage() {
  const navigate = useNavigate(); const { result: analysis } = useFinancialAnalysis(); const { setResult, clearResult } = useExpenseSimulation()
  const [formState, setFormState] = useState(initialState); const [errors, setErrors] = useState<ExpenseSimulationFormErrors>({}); const [isSubmitting, setIsSubmitting] = useState(false); const [submitError, setSubmitError] = useState('')
  if (!analysis) return <section className="empty-result"><span className="eyebrow">Análise necessária</span><h1>Faça sua análise antes de simular</h1><p>A simulação usa o resultado da sua análise financeira atual para apresentar a comparação entre os cenários.</p><div className="result-actions"><ButtonLink to="/analysis">Fazer análise financeira</ButtonLink><ButtonLink to="/" variant="secondary">Voltar para o início</ButtonLink></div></section>
  const profile = financialProfileDetails[analysis.financialProfile]
  const updateField = (field: keyof ExpenseSimulationFormState, value: string) => setFormState((current) => ({ ...current, [field]: value }))
  const submit = async (event: FormEvent) => { event.preventDefault(); if (isSubmitting) return; const nextErrors = validateExpenseSimulationForm(formState); setErrors(nextErrors); setSubmitError(''); if (hasExpenseSimulationFormErrors(nextErrors)) return; setIsSubmitting(true); clearResult(); try { setResult(await simulateExpense(analysis, formState)); navigate('/expense-simulation/result') } catch { setSubmitError('Não foi possível concluir a simulação agora. Tente novamente.'); setIsSubmitting(false) } }
  const metrics = [{ label: 'Renda média', value: formatCurrency(analysis.metrics.averageIncome) }, { label: 'Endividamento', value: formatPercentage(analysis.metrics.debtRatioPercentage) }, { label: 'Comprometimento fixo', value: formatPercentage(analysis.metrics.fixedCommitmentPercentage) }]
  return <div className="analysis-page"><header className="page-heading"><span className="eyebrow">Segunda etapa</span><h1>Simule uma nova despesa</h1><p>Partimos da análise financeira já realizada. Agora, informe somente os dados da compra que deseja avaliar.</p></header><section className="analysis-recap" aria-labelledby="analysis-recap-title"><div><span>Situação atual</span><h2 id="analysis-recap-title">{profile.name}</h2><p>{formatProbability(analysis.probability)} de probabilidade do perfil</p></div><dl>{metrics.map((metric) => <div key={metric.label}><dt>{metric.label}</dt><dd>{metric.value}</dd></div>)}</dl></section><aside className="prototype-notice"><strong>Estrutura provisória do protótipo</strong><p>O resultado será demonstrativo e não será calculado a partir dos dados informados. A integração real com a API ainda será implementada.</p></aside><form className="analysis-form" onSubmit={submit} noValidate><NewExpenseSection description={formState.expenseDescription} totalAmount={formState.totalAmount} installments={formState.installments} errors={errors} onChange={updateField} />{submitError && <div className="submit-error" role="alert">{submitError}</div>}<div className="form-footer"><p aria-live="polite">{isSubmitting ? 'Sua simulação está sendo processada.' : 'A análise atual permanecerá salva durante a simulação.'}</p><div><ButtonLink to="/analysis/result" variant="secondary">Voltar ao resultado</ButtonLink><button className="button button-primary" type="submit" disabled={isSubmitting}>{isSubmitting ? 'Simulando...' : 'Simular nova despesa'}</button></div></div></form></div>
}
