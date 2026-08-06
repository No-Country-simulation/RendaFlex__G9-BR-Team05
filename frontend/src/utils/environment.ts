export function getApiBaseUrl() {
  return (import.meta.env.VITE_API_BASE_URL ?? '').replace(/\/+$/, '')
}

export function isMockApiEnabled() {
  return import.meta.env.VITE_USE_MOCK_API === 'true'
}
