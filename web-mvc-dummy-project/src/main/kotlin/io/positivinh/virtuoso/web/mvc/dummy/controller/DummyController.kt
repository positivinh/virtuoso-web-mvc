package io.positivinh.virtuoso.web.mvc.dummy.controller

import com.crabshue.commons.exceptions.ApplicationException
import io.positivinh.virtuoso.domain.validations.exceptions.EntityErrorType
import io.positivinh.virtuoso.domain.validations.exceptions.ValidationException
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Throws the exceptions the error-handling tests answer.
 */
@RestController
@RequestMapping("/api/dummy")
class DummyController {

    companion object {

        const val USERNAME = "john"
        const val SECRET = "secret-password"
    }

    data class Credentials(val username: String, val password: String)

    @GetMapping("/application-error")
    fun applicationError(): String {

        throw ApplicationException(EntityErrorType.ENTITY_INVALID, "Something is wrong")
            .addContextValue("USERNAME", USERNAME)
            .addContextValue("ENTITY", Credentials(USERNAME, SECRET))
    }

    @GetMapping("/validation-error")
    fun validationError(): String {

        val errors = BeanPropertyBindingResult(Credentials(USERNAME, SECRET), "credentials")
            .apply { rejectValue("password", "weak", "Password is too weak") }

        throw ValidationException(EntityErrorType.ENTITY_INVALID, errors)
    }

    @GetMapping("/not-found")
    fun notFound(): String {

        throw DummyNotFoundException(USERNAME)
    }
}
