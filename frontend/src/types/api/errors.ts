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
  message: string
}

export type ApiError = {
  code: ApiErrorCode
  message: string
  fieldErrors: ApiFieldError[]
}
