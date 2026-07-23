export type FieldError = {
  field: string
  code: string
  message: string
  rejectedValue?: string | number | boolean | null
}

export type ApiError = {
  timestamp: string
  status: number
  error: string
  code: string
  message: string
  path: string
  fieldErrors: FieldError[]
}
