import type { ApiError } from '../types'
import { getApiBaseUrl } from '../utils/environment'

const REQUEST_TIMEOUT_MS = 20_000

export class HttpError extends Error {
  readonly status: number
  readonly body: unknown
  readonly apiError?: ApiError

  constructor(status: number, message: string, body: unknown, apiError?: ApiError) {
    super(message)
    this.name = 'HttpError'
    this.status = status
    this.body = body
    this.apiError = apiError
  }
}

export class NetworkError extends Error {
  readonly cause: unknown

  constructor(cause: unknown) {
    super('Não foi possível conectar à API.')
    this.name = 'NetworkError'
    this.cause = cause
  }
}

export class TimeoutError extends Error {
  readonly timeoutMs: number

  constructor(timeoutMs: number) {
    super(`A requisição excedeu o tempo limite de ${timeoutMs} ms.`)
    this.name = 'TimeoutError'
    this.timeoutMs = timeoutMs
  }
}

export class InvalidResponseError extends Error {
  readonly status: number
  readonly body: string

  constructor(status: number, body: string) {
    super('A API retornou uma resposta JSON inválida.')
    this.name = 'InvalidResponseError'
    this.status = status
    this.body = body
  }
}

type JsonRequestOptions = {
  method?: 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE'
  body?: unknown
}

function buildUrl(path: string) {
  const normalizedPath = path.startsWith('/') ? path : `/${path}`
  return `${getApiBaseUrl()}${normalizedPath}`
}

function isApiError(value: unknown): value is ApiError {
  if (!value || typeof value !== 'object') return false
  const candidate = value as Partial<ApiError>
  return typeof candidate.timestamp === 'string'
    && typeof candidate.status === 'number'
    && typeof candidate.error === 'string'
    && typeof candidate.code === 'string'
    && typeof candidate.message === 'string'
    && typeof candidate.path === 'string'
    && Array.isArray(candidate.fieldErrors)
}

function parseJson(text: string) {
  try {
    return { parsed: true as const, value: JSON.parse(text) as unknown }
  } catch {
    return { parsed: false as const }
  }
}

export async function requestJson<T>(path: string, options: JsonRequestOptions = {}): Promise<T> {
  const controller = new AbortController()
  let timedOut = false
  const timeoutId = window.setTimeout(() => {
    timedOut = true
    controller.abort()
  }, REQUEST_TIMEOUT_MS)

  let response: Response
  try {
    response = await fetch(buildUrl(path), {
      method: options.method ?? 'GET',
      headers: { 'Content-Type': 'application/json' },
      body: options.body === undefined ? undefined : JSON.stringify(options.body),
      signal: controller.signal,
    })
  } catch (error) {
    if (timedOut) throw new TimeoutError(REQUEST_TIMEOUT_MS)
    throw new NetworkError(error)
  } finally {
    window.clearTimeout(timeoutId)
  }

  if (response.status === 204) return undefined as T

  const responseText = await response.text()
  const parsedBody = responseText ? parseJson(responseText) : undefined

  if (!response.ok) {
    const body = parsedBody?.parsed ? parsedBody.value : responseText || undefined
    const apiError = isApiError(body) ? body : undefined
    throw new HttpError(
      response.status,
      apiError?.message ?? `A API respondeu com o status HTTP ${response.status}.`,
      body,
      apiError,
    )
  }

  if (!responseText) return undefined as T
  if (!parsedBody?.parsed) throw new InvalidResponseError(response.status, responseText)
  return parsedBody.value as T
}
