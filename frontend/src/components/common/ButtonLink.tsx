import type { ReactNode } from 'react'
import { Link } from 'react-router-dom'

type ButtonLinkProps = { children: ReactNode; to: string; variant?: 'primary' | 'secondary'; icon?: ReactNode }

export function ButtonLink({ children, to, variant = 'primary', icon }: ButtonLinkProps) {
  return <Link className={`button button-${variant}`} to={to}><span className="button-content">{children}{icon}</span></Link>
}
