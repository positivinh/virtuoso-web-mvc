package io.positivinh.virtuoso.web.mvc.dummy.controller

import io.positivinh.virtuoso.web.mvc.problem.ProblemDetailFactory
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * How a service maps its own exceptions: ordered before the starter's fallback advice, which maps any other
 * `ApplicationException` to `400`.
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class DummyControllerAdvice(private val problemDetailFactory: ProblemDetailFactory) {

    @ExceptionHandler(DummyNotFoundException::class)
    fun handleDummyNotFoundException(exception: DummyNotFoundException): ProblemDetail {

        return problemDetailFactory.create(exception, HttpStatus.NOT_FOUND)
    }
}
