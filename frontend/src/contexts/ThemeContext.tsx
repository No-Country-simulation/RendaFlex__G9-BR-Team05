import { useEffect, useState, type ReactNode } from 'react'
import { ThemeContext, type Theme } from './themeContextValue'

const storageKey = 'rendaflex.theme'

function getInitialTheme(): Theme {
  const documentTheme = document.documentElement.dataset.theme
  if (documentTheme === 'light' || documentTheme === 'dark') return documentTheme
  return window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light'
}

export function ThemeProvider({ children }: { children: ReactNode }) {
  const [theme, setTheme] = useState<Theme>(getInitialTheme)

  useEffect(() => {
    document.documentElement.dataset.theme = theme
    document.documentElement.style.colorScheme = theme
    localStorage.setItem(storageKey, theme)
  }, [theme])

  const toggleTheme = () => setTheme((current) => current === 'light' ? 'dark' : 'light')

  return <ThemeContext.Provider value={{ theme, toggleTheme }}>{children}</ThemeContext.Provider>
}
