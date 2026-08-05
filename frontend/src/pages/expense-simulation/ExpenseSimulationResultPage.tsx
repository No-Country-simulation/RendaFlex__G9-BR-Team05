import { useNavigate } from 'react-router-dom'
import { ButtonLink } from '../../components/common/ButtonLink'
import { RecommendationsSection } from '../../components/financial-analysis/RecommendationsSection'
import { useExpenseSimulation } from '../../hooks/useExpenseSimulation'
import type { FinancialMetrics } from '../../types'
import { financialProfileDetails, formatCurrency, formatPercentage, formatProbability } from '../../utils/financialAnalysisPresentation'

const metrics = (current: FinancialMetrics, projected: FinancialMetrics) => [
  { label: 'Renda média', current: current.averageIncome, projected: projected.averageIncome, format: formatCurrency },
  { label: 'Variação da renda', current: current.incomeVariationCoefficientPercentage, projected: projected.incomeVariationCoefficientPercentage, format: formatPercentage },
  { label: 'Nível de endividamento', current: current.debtRatioPercentage, projected: projected.debtRatioPercentage, format: formatPercentage },
  { label: 'Comprometimento fixo', current: current.fixedCommitmentPercentage, projected: projected.fixedCommitmentPercentage, format: formatPercentage },
]

export function ExpenseSimulationResultPage() {
  const { result, clearResult } = useExpenseSimulation(); const navigate = useNavigate()
  if (!result) return <section className="empty-result"><span className="eyebrow">Resultado indisponível</span><h1>Realize uma simulação primeiro</h1><p>Não há uma simulação disponível nesta sessão.</p><div className="result-actions"><ButtonLink to="/expense-simulation">Ir para a simulação</ButtonLink><ButtonLink to="/" variant="secondary">Voltar para o início</ButtonLink></div></section>
  const currentProfile = financialProfileDetails[result.currentProfile]; const projectedProfile = financialProfileDetails[result.projectedProfile]
  const startAgain = () => { clearResult(); navigate('/expense-simulation') }
  return <div className="result-page">
    <header className="page-heading"><span className="eyebrow">Resultado da simulação</span><h1>Comparação dos cenários</h1><p>Veja o possível impacto demonstrativo da despesa informada.</p></header>
    <aside className="demo-notice"><strong>Este resultado é demonstrativo.</strong><p>Os indicadores não foram calculados a partir dos dados preenchidos.</p><p>A integração real com a API ainda será implementada.</p></aside>
    <section className="expense-summary" aria-labelledby="expense-summary-title"><div><span>Despesa simulada</span><h2 id="expense-summary-title">{result.simulatedExpense.description}</h2></div><dl><div><dt>Valor total</dt><dd>{formatCurrency(result.simulatedExpense.totalAmount)}</dd></div><div><dt>Parcelas</dt><dd>{result.simulatedExpense.installments}</dd></div><div><dt>Valor mensal</dt><dd>{formatCurrency(result.simulatedExpense.monthlyAmount)}</dd></div></dl></section>
    <section className="scenario-grid" aria-labelledby="scenario-title"><h2 className="visually-hidden" id="scenario-title">Perfis atual e projetado</h2><article className="scenario-card"><span>Cenário atual</span><h2>{currentProfile.name}</h2><strong>{formatProbability(result.currentProbability)}</strong><p>Probabilidade do perfil</p></article><article className="scenario-card projected"><span>Cenário projetado</span><h2>{projectedProfile.name}</h2><strong>{formatProbability(result.projectedProbability)}</strong><p>Probabilidade do perfil</p></article></section>
    <p className={`change-summary ${result.profileChanged ? 'change-worse' : ''}`}><strong>{result.profileChanged ? 'Mudança de perfil identificada' : 'Perfil mantido'}</strong><span>{result.profileChanged ? `O cenário passa de ${currentProfile.name} para ${projectedProfile.name}. O resultado indica piora e merece atenção.` : 'A projeção mantém o perfil atual.'}</span></p>
    <section className="installment-highlight" aria-labelledby="installment-result-title"><span id="installment-result-title">Valor mensal da nova despesa</span><strong>{formatCurrency(result.monthlyInstallmentAmount)}</strong></section>
    <section className="result-section" aria-labelledby="comparison-title"><div className="result-section-heading"><h2 id="comparison-title">Indicadores atuais e projetados</h2><p>A diferença mostra a variação do cenário projetado.</p></div><div className="comparison-grid">{metrics(result.currentMetrics, result.projectedMetrics).map((metric) => { const difference = metric.projected - metric.current; const direction = difference > 0 ? 'Aumento' : difference < 0 ? 'Redução' : 'Estável'; return <article className="comparison-card" key={metric.label}><h3>{metric.label}</h3><dl><div><dt>Atual</dt><dd>{metric.format(metric.current)}</dd></div><div><dt>Projetado</dt><dd>{metric.format(metric.projected)}</dd></div><div><dt>Diferença</dt><dd>{direction}: {metric.format(Math.abs(difference))}</dd></div></dl></article> })}</div></section>
    <RecommendationsSection recommendations={result.recommendations} />
    <nav className="result-actions" aria-label="Ações do resultado"><button className="button button-primary" type="button" onClick={startAgain}>Fazer nova simulação</button><ButtonLink to="/analysis/result" variant="secondary">Ver análise atual</ButtonLink><ButtonLink to="/" variant="secondary">Voltar para o início</ButtonLink></nav>
  </div>
}
