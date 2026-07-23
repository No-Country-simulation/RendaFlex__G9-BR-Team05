import { Link } from 'react-router-dom'
import { Navigation } from './Navigation'

export function Header() {
  return (
    <header className="app-header">
      <div className="header-content">
        <Link className="brand" to="/" aria-label="RendaFlex — página inicial">
          <span className="brand-mark" aria-hidden="true">R</span>
          <span>RendaFlex</span>
        </Link>
        <Navigation />
      </div>
    </header>
  )
}
