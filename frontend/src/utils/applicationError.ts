import { ApiErrorCode, type ApiFieldError, type ApiErrorCode as ApiErrorCodeValue } from '../types'
import { HttpError, InvalidResponseError, NetworkError, TimeoutError } from '../services/apiClient'

export type ApplicationErrorKind =
  | 'VALIDATION'
  | 'BUSINESS'
  | 'UNAVAILABLE'
  | 'TIMEOUT'
  | 'INVALID_RESPONSE'
  | 'NETWORK'
  | 'INTERNAL'
  | 'UNKNOWN'

export type ApplicationError = {
  message: string
  code?: ApiErrorCodeValue
  status?: number
  fieldErrors: ApiFieldError[]
  kind: ApplicationErrorKind
}

const unsafeMessagePattern = /https?:\/\/|stack\s*trace|exception|container|\.(?:java|py)(?::\d+)?/i

function safeApiMessage(message: string, fallback: string) {
  const normalizedMessage = message.trim()
  return normalizedMessage && normalizedMessage.length <= 300 && !unsafeMessagePattern.test(normalizedMessage)
    ? normalizedMessage
    : fallback
}

function safeFieldErrors(fieldErrors: ApiFieldError[]) {
  return fieldErrors.map((fieldError) => ({
    ...fieldError,
    message: safeApiMessage(fieldError.message, 'Valor informado inválido.'),
  }))
}

function fromStatus(status: number): ApplicationError {
  if (status === 400) return { kind: 'VALIDATION', message: 'Revise os dados informados.', status, fieldErrors: [] }
  if (status === 422) return { kind: 'BUSINESS', message: 'Não foi possível processar a análise com os dados informados.', status, fieldErrors: [] }
  if (status === 502) return { kind: 'INVALID_RESPONSE', message: 'O serviço retornou uma resposta inválida.', status, fieldErrors: [] }
  if (status === 503) return { kind: 'UNAVAILABLE', message: 'O serviço de análise está temporariamente indisponível. Tente novamente em instantes.', status, fieldErrors: [] }
  if (status === 500) return { kind: 'INTERNAL', message: 'Ocorreu um erro inesperado. Tente novamente.', status, fieldErrors: [] }
  return { kind: 'UNKNOWN', message: 'Ocorreu um erro inesperado. Tente novamente.', status, fieldErrors: [] }
}

function fromHttpError(error: HttpError): ApplicationError {
  const apiError = error.apiError
  if (!apiError) return fromStatus(error.status)

  const fieldErrors = safeFieldErrors(apiError.fieldErrors)
  const common = { code: apiError.code, status: error.status, fieldErrors }

  if (apiError.code === ApiErrorCode.VALIDATION_ERROR) return { ...common, kind: 'VALIDATION', message: safeApiMessage(apiError.message, 'Revise os dados informados.') }
  if (apiError.code === ApiErrorCode.BUSINESS_RULE_ERROR) return { ...common, kind: 'BUSINESS', message: safeApiMessage(apiError.message, 'Não foi possível processar a análise com os dados informados.') }
  if (apiError.code === ApiErrorCode.MODEL_SERVICE_UNAVAILABLE) return { ...common, kind: 'UNAVAILABLE', message: safeApiMessage(apiError.message, 'O serviço de análise está temporariamente indisponível. Tente novamente em instantes.') }
  if (apiError.code === ApiErrorCode.MODEL_SERVICE_TIMEOUT) return { ...common, kind: 'TIMEOUT', message: safeApiMessage(apiError.message, 'A análise demorou mais que o esperado. Tente novamente.') }
  if (apiError.code === ApiErrorCode.MODEL_SERVICE_INVALID_RESPONSE) return { ...common, kind: 'INVALID_RESPONSE', message: safeApiMessage(apiError.message, 'O serviço de análise retornou uma resposta inválida.') }
  if (apiError.code === ApiErrorCode.INTERNAL_ERROR) return { ...common, kind: 'INTERNAL', message: safeApiMessage(apiError.message, 'Ocorreu um erro inesperado. Tente novamente.') }

  return fromStatus(error.status)
}

export function resolveApplicationError(error: unknown): ApplicationError {
  if (error instanceof HttpError) return fromHttpError(error)
  if (error instanceof TimeoutError) return { kind: 'TIMEOUT', message: 'A solicitação demorou mais que o esperado. Tente novamente.', fieldErrors: [] }
  if (error instanceof NetworkError) return { kind: 'NETWORK', message: 'Não foi possível conectar ao serviço. Verifique sua conexão e tente novamente.', fieldErrors: [] }
  if (error instanceof InvalidResponseError) return { kind: 'INVALID_RESPONSE', message: 'O serviço retornou uma resposta inválida.', status: error.status, fieldErrors: [] }
  return { kind: 'UNKNOWN', message: 'Ocorreu um erro inesperado. Tente novamente.', fieldErrors: [] }
}
