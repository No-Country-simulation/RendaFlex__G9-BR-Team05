import { BrowserRouter, Route, Routes } from 'react-router-dom'
import { AppLayout } from '../layouts/AppLayout'
import { FinancialAnalysisPage } from '../pages/financial-analysis/FinancialAnalysisPage'
import { FinancialAnalysisResultPage } from '../pages/financial-analysis/FinancialAnalysisResultPage'
import { HomePage } from '../pages/home/HomePage'
import { ExpenseSimulationPage } from '../pages/expense-simulation/ExpenseSimulationPage'
import { ExpenseSimulationResultPage } from '../pages/expense-simulation/ExpenseSimulationResultPage'

export function AppRoutes() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<AppLayout />}>
          <Route path="/" element={<HomePage />} />
          <Route path="/analysis" element={<FinancialAnalysisPage />} />
          <Route
            path="/analysis/result"
            element={<FinancialAnalysisResultPage />}
          />
          <Route path="/expense-simulation" element={<ExpenseSimulationPage />} />
          <Route path="/expense-simulation/result" element={<ExpenseSimulationResultPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  )
}
