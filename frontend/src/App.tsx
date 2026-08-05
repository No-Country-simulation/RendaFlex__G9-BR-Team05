import { AppRoutes } from './routes/AppRoutes'
import { FinancialAnalysisProvider } from './contexts/FinancialAnalysisContext'
import { ExpenseSimulationProvider } from './contexts/ExpenseSimulationContext'

function App() {
  return <FinancialAnalysisProvider><ExpenseSimulationProvider><AppRoutes /></ExpenseSimulationProvider></FinancialAnalysisProvider>
}

export default App
