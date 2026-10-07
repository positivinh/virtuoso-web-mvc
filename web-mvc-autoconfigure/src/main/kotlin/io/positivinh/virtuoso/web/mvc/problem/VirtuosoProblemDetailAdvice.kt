package io.positivinh.virtuoso.web.mvc.problem

import com.crabshue.commons.exceptions.ApplicationException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Fallback mapping of [ApplicationException]s (including validation errors) to `400 Bad Request`.
 *
 * It runs after every other advice: Spring MVC picks the first advice, in order, that has a handler for the
 * exception. A service maps its own exceptions (404, 403, 409, ...) in an advice annotated
 * `@Order(Ordered.HIGHEST_PRECEDENCE)`, using [ProblemDetailFactory].
 *
 * `AccessDeniedException` is not handled here: Spring Security answers it with `403`.
 */
@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
class VirtuosoProblemDetailAdvice(private val problemDetailFactory: ProblemDetailFactory) {

    @ExceptionHandler(ApplicationException::class)
    fun handleApplicationException(exception: ApplicationException): ProblemDetail {

        return problemDetailFactory.create(exception, HttpStatus.BAD_REQUEST)
    }
}
