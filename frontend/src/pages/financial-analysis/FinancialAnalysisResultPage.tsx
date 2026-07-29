import { useNavigate } from 'react-router-dom'
import { ButtonLink } from '../../components/common/ButtonLink'
import { useFinancialAnalysis } from '../../hooks/useFinancialAnalysis'
import { FinancialProfile, type FinancialProfile as FinancialProfileValue } from '../../types'

const profiles: Record<FinancialProfileValue, { name: string; description: string }> = {
  [FinancialProfile.HEALTHY]: { name: 'Saudável', description: 'Seus indicadores mostram uma situação financeira equilibrada.' },
  [FinancialProfile.UNDER_OBSERVATION]: { name: 'Em observação', description: 'Alguns indicadores merecem atenção para preservar sua flexibilidade financeira.' },
  [FinancialProfile.AT_RISK]: { name: 'Em risco', description: 'Seus indicadores apontam a necessidade de rever compromissos financeiros.' },
}
const currency = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' })
const percentage = new Intl.NumberFormat('pt-BR', { maximumFractionDigits: 2 })

export function FinancialAnalysisResultPage() {
  const { result, clearResult } = useFinancialAnalysis(); const navigate = useNavigate()
  if (!result) return <section className="empty-result"><span className="eyebrow">Resultado indisponível</span><h1>Realize uma análise primeiro</h1><p>Não há um resultado disponível nesta sessão. Preencha o formulário para gerar uma análise.</p><div className="result-actions"><ButtonLink to="/analysis">Ir para a análise</ButtonLink><ButtonLink to="/" variant="secondary">Voltar para o início</ButtonLink></div></section>
  const profile = profiles[result.financialProfile]
  const metrics = [{ label: 'Renda média', value: currency.format(result.metrics.averageIncome) }, { label: 'Variação da renda', value: `${percentage.format(result.metrics.incomeVariationCoefficientPercentage)}%` }, { label: 'Nível de endividamento', value: `${percentage.format(result.metrics.debtRatioPercentage)}%` }, { label: 'Comprometimento fixo', value: `${percentage.format(result.metrics.fixedCommitmentPercentage)}%` }]
  const startAgain = () => { clearResult(); navigate('/analysis') }
  return <div className="result-page"><header className="page-heading"><span className="eyebrow">Resultado da análise</span><h1>Seu perfil financeiro</h1><p>Este resultado demonstra a futura resposta pública da análise, sem cálculos realizados no navegador.</p></header><section className={`profile-summary profile-${result.financialProfile.toLowerCase()}`}><div><span>Perfil financeiro</span><h2>{profile.name}</h2><p>{profile.description}</p></div><div className="probability"><strong>{percentage.format(result.probability * 100)}%</strong><span>probabilidade do perfil</span></div></section><section className="metrics-section" aria-labelledby="metrics-title"><h2 id="metrics-title">Indicadores financeiros</h2><div className="metrics-grid">{metrics.map((metric) => <article className="metric-card" key={metric.label}><span>{metric.label}</span><strong>{metric.value}</strong></article>)}</div></section><div className="result-actions"><button className="button button-primary" type="button" onClick={startAgain}>Fazer nova análise</button><ButtonLink to="/" variant="secondary">Voltar para o início</ButtonLink></div></div>
}
