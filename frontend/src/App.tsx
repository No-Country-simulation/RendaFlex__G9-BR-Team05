import { AppRoutes } from './routes/AppRoutes'
import { FinancialAnalysisProvider } from './contexts/FinancialAnalysisContext'

function App() {
  return <FinancialAnalysisProvider><AppRoutes /></FinancialAnalysisProvider>
}

export default App
