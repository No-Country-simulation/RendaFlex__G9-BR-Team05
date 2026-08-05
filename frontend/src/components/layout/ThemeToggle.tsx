import { useTheme } from '../../hooks/useTheme'

export function ThemeToggle() {
  const { theme, toggleTheme } = useTheme()
  const isDark = theme === 'dark'

  return <button className="theme-toggle" type="button" onClick={toggleTheme} aria-label={`Ativar modo ${isDark ? 'claro' : 'escuro'}`} aria-pressed={isDark}><span aria-hidden="true">{isDark ? '☾' : '☀'}</span><span>Modo {isDark ? 'escuro' : 'claro'}</span></button>
}
