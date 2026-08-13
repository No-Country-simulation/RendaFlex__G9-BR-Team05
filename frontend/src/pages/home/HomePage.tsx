import { ButtonLink } from '../../components/common/ButtonLink'
import { Card } from '../../components/common/Card'

export function HomePage() {
  return (
    <div className="home-page">
      <section className="hero" aria-labelledby="hero-title">
        <div className="hero-content">
          <span className="eyebrow">Sua renda muda. Seu planejamento pode acompanhar.</span>
          <h1 id="hero-title">Entenda melhor sua vida financeira, mês a mês.</h1>
          <p>O RendaFlex ajuda quem tem renda variável a organizar informações e tomar decisões com mais clareza e confiança.</p>
          <div className="hero-actions"><ButtonLink to="/analysis">Começar minha análise</ButtonLink></div>
        </div>
        <aside className="hero-note" aria-label="Proposta do RendaFlex">
          <strong>Feito para a sua realidade</strong>
          <p>Uma visão simples que considera as mudanças da sua renda ao longo do tempo.</p>
        </aside>
      </section>

      <section className="home-section" aria-labelledby="features-title">
        <div className="section-heading"><span className="eyebrow">Recursos</span><h2 id="features-title">Mais contexto para cuidar do seu dinheiro</h2></div>
        <div className="card-grid">
          <Card><h3>Perfil financeiro</h3><p>Entenda seu momento a partir do histórico da sua renda.</p></Card>
          <Card><h3>Despesas organizadas</h3><p>Visualize seus gastos por categorias de forma simples.</p></Card>
          <Card><h3>Recomendações</h3><p>Receba orientações práticas adequadas ao seu contexto.</p></Card>
          <Card><h3>Simulação de despesas</h3><p>Depois da análise, compare o cenário atual e o impacto possível de uma nova despesa.</p><ButtonLink to="/expense-simulation" variant="secondary">Continuar para a simulação</ButtonLink></Card>
        </div>
      </section>

      <section className="home-section steps-section" aria-labelledby="steps-title">
        <div className="section-heading"><span className="eyebrow">Como funciona</span><h2 id="steps-title">Da análise à simulação</h2></div>
        <ol className="steps-list">
          <li><span>1</span><div><h3>Informe seu histórico</h3><p>Conte como sua renda variou nos últimos meses.</p></div></li>
          <li><span>2</span><div><h3>Registre seu contexto</h3><p>Adicione informações importantes da sua rotina financeira.</p></div></li>
          <li><span>3</span><div><h3>Simule uma nova despesa</h3><p>Use o resultado da análise para comparar o cenário atual com uma projeção.</p></div></li>
        </ol>
      </section>

      <aside className="disclaimer"><strong>Uma ferramenta de orientação</strong><p>O RendaFlex apoia sua organização e não substitui uma consultoria financeira profissional.</p></aside>
    </div>
  )
}
