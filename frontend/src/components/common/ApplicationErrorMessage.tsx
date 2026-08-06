import type { ApplicationError } from '../../utils/applicationError'

type Props = {
  error: ApplicationError
}

export function ApplicationErrorMessage({ error }: Props) {
  const fieldMessages = [...new Set(error.fieldErrors.map((fieldError) => fieldError.message))]
  return <div className="submit-error" role="alert"><p>{error.message}</p>{fieldMessages.length > 0 && <ul>{fieldMessages.map((message) => <li key={message}>{message}</li>)}</ul>}</div>
}
