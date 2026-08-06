export const ApiErrorCode = {
  VALIDATION_ERROR: 'VALIDATION_ERROR',
  BUSINESS_RULE_ERROR: 'BUSINESS_RULE_ERROR',
  INTERNAL_ERROR: 'INTERNAL_ERROR',
  MODEL_SERVICE_UNAVAILABLE: 'MODEL_SERVICE_UNAVAILABLE',
  MODEL_SERVICE_TIMEOUT: 'MODEL_SERVICE_TIMEOUT',
  MODEL_SERVICE_INVALID_RESPONSE: 'MODEL_SERVICE_INVALID_RESPONSE',
} as const

export type ApiErrorCode =
  (typeof ApiErrorCode)[keyof typeof ApiErrorCode]

export type ApiFieldError = {
  field: string
  code: string
  message: string
  rejectedValue?: string | number | boolean | null
}

export type ApiError = {
  timestamp: string
  status: number
  error: string
  code: ApiErrorCode
  message: string
  path: string
  fieldErrors: ApiFieldError[]
}
