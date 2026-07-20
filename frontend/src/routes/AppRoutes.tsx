import { BrowserRouter, Route, Routes } from 'react-router-dom'
import { AppLayout } from '../layouts/AppLayout'
import { FinancialAnalysisPage } from '../pages/financial-analysis/FinancialAnalysisPage'
import { FinancialAnalysisResultPage } from '../pages/financial-analysis/FinancialAnalysisResultPage'
import { HomePage } from '../pages/home/HomePage'

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
        </Route>
      </Routes>
    </BrowserRouter>
  )
}
