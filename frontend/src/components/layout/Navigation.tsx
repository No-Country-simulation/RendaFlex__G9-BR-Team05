import { NavLink } from 'react-router-dom'

export function Navigation() {
  return (
    <nav className="main-navigation" aria-label="Navegação principal">
      <NavLink to="/" end>Início</NavLink>
      <NavLink to="/analysis">Fazer análise</NavLink>
      <NavLink to="/expense-simulation">Simular despesa</NavLink>
    </nav>
  )
}
