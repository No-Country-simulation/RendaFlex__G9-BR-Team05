import { SavingFrequency, type SavingFrequency as SavingFrequencyValue } from '../../types'

type Props = { value: SavingFrequencyValue | ''; onChange: (value: SavingFrequencyValue) => void }

const options = [
  { value: SavingFrequency.LOW, label: 'Baixa', description: 'Raramente consigo guardar' },
  { value: SavingFrequency.MEDIUM, label: 'Média', description: 'Guardo em alguns meses' },
  { value: SavingFrequency.HIGH, label: 'Alta', description: 'Guardo com frequência' },
]

export function SavingFrequencyField({ value, onChange }: Props) {
  return (
    <section className="form-section" aria-labelledby="saving-title">
      <div className="form-section-heading"><div><span className="step-number">2</span><h2 id="saving-title">Frequência de poupança</h2></div><p>Com que frequência você consegue guardar parte da sua renda?</p></div>
      <fieldset className="radio-group"><legend className="visually-hidden">Selecione a frequência de poupança</legend>
        {options.map((option) => <label className="radio-card" key={option.value}><input type="radio" name="savingFrequency" value={option.value} checked={value === option.value} required onChange={() => onChange(option.value)} /><span><strong>{option.label}</strong><small>{option.description}</small></span></label>)}
      </fieldset>
    </section>
  )
}
