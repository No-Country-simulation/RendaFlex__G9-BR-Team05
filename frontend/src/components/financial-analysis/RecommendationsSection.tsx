import type { Recommendation } from '../../types'
import { recommendationPriorityLabels } from '../../utils/financialAnalysisPresentation'

type RecommendationsSectionProps = {
  recommendations: Recommendation[]
}

export function RecommendationsSection({ recommendations }: RecommendationsSectionProps) {
  return (
    <section className="result-section" aria-labelledby="recommendations-title">
      <div className="result-section-heading">
        <h2 id="recommendations-title">Recomendações financeiras</h2>
        <p>Orientações demonstrativas associadas aos indicadores apresentados.</p>
      </div>
      {recommendations.length > 0 ? (
        <ul className="recommendations-list">
          {recommendations.map((recommendation, index) => (
            <li className="recommendation-card" key={`${recommendation.priority}-${index}`}>
              <span className={`priority-label priority-${recommendation.priority.toLowerCase()}`}>
                Prioridade {recommendationPriorityLabels[recommendation.priority]}
              </span>
              <p>{recommendation.message}</p>
            </li>
          ))}
        </ul>
      ) : (
        <p className="section-empty-state">Nenhuma recomendação está disponível para esta análise.</p>
      )}
    </section>
  )
}
