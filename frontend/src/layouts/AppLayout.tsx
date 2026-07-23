import { Outlet } from 'react-router-dom'
import { Footer } from '../components/layout/Footer'
import { Header } from '../components/layout/Header'

export function AppLayout() {
  return (
    <div className="app-layout">
      <Header />
      <main className="main-content"><Outlet /></main>
      <Footer />
    </div>
  )
}
