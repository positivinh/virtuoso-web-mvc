package io.positivinh.virtuoso.web.mvc.problem

import com.crabshue.commons.exceptions.AbstractException
import com.crabshue.commons.exceptions.context.CommonErrorContext
import com.crabshue.commons.kotlin.logging.getLogger
import io.positivinh.virtuoso.domain.validations.exceptions.ValidationException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.validation.FieldError

/**
 * Builds RFC 7807 [ProblemDetail] responses from [AbstractException]s without leaking internals.
 *
 * - Only allow-listed context entries are copied into the response: [CommonErrorContext.EXCEPTION_MESSAGE] and the
 *   given [exposedContextKeys]. Any other entry (entity, cause, ...) may carry secrets, personal data or internals.
 * - A [ValidationException] adds an [ERRORS_PROPERTY] list of field, code and message, never the rejected values.
 * - Client errors are logged at `WARN` without a stack trace, server errors at `ERROR` with it.
 *
 * Use it from a service's `@RestControllerAdvice` to map service-specific exceptions to their HTTP status.
 */
class ProblemDetailFactory(exposedContextKeys: Collection<String> = emptySet()) {

    companion object {

        const val ERRORS_PROPERTY = "errors"
    }

    private val log = getLogger()

    private val exposedContextKeys: Set<String> = exposedContextKeys.toSet() + CommonErrorContext.EXCEPTION_MESSAGE.name

    /**
     * Builds the [ProblemDetail] answering [exception] with [status], and logs the exception.
     */
    fun create(exception: AbstractException, status: HttpStatus): ProblemDetail {

        logException(exception, status)

        return ProblemDetail.forStatusAndDetail(status, exception.rawMessage)
            .apply {
                exception.contextEntries
                    .filter { it.key in exposedContextKeys }
                    .forEach { this.setProperty(it.key, it.value) }

                if (exception is ValidationException) {
                    this.setProperty(ERRORS_PROPERTY, validationErrors(exception))
                }
            }
    }

    private fun logException(exception: AbstractException, status: HttpStatus) {

        // the message of a validation exception lists the rejected values, which may be a password or personal data
        val description: Any? =
            if (exception is ValidationException) validationErrors(exception) else exception.rawMessage

        if (status.is5xxServerError) {
            log.error(
                "Answered [{}] to [{}]: [{}]",
                status.value(),
                exception.javaClass.simpleName,
                description,
                exception
            )
        } else {
            log.warn("Answered [{}] to [{}]: [{}]", status.value(), exception.javaClass.simpleName, description)
        }
    }

    /**
     * Validation errors without their rejected values.
     */
    private fun validationErrors(exception: ValidationException): List<Map<String, String?>> {

        return exception.errors.allErrors.map {
            mapOf(
                "field" to (it as? FieldError)?.field,
                "code" to it.code,
                "message" to it.defaultMessage,
            )
        }
    }
}
