import { AppRoutes } from './routes/AppRoutes'
import { FinancialAnalysisProvider } from './contexts/FinancialAnalysisContext'
import { ExpenseSimulationProvider } from './contexts/ExpenseSimulationContext'
import { ThemeProvider } from './contexts/ThemeContext'

function App() {
  return <ThemeProvider><FinancialAnalysisProvider><ExpenseSimulationProvider><AppRoutes /></ExpenseSimulationProvider></FinancialAnalysisProvider></ThemeProvider>
}

export default App
