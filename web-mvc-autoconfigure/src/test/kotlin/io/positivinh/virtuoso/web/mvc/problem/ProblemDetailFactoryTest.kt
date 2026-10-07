package io.positivinh.virtuoso.web.mvc.problem

import com.crabshue.commons.exceptions.ApplicationException
import com.crabshue.commons.exceptions.context.CommonErrorContext
import io.positivinh.virtuoso.domain.validations.exceptions.EntityErrorType
import io.positivinh.virtuoso.domain.validations.exceptions.ValidationException
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.validation.BeanPropertyBindingResult

class ProblemDetailFactoryTest {

    data class Credentials(val username: String, val password: String)

    private val problemDetailFactory = ProblemDetailFactory(setOf("USERNAME"))

    @Test
    fun create_setsStatusAndDetail() {

        val exception = ApplicationException(EntityErrorType.ENTITY_INVALID, "Something is wrong")

        val problemDetail = problemDetailFactory.create(exception, HttpStatus.NOT_FOUND)

        Assertions.assertThat(problemDetail.status).isEqualTo(HttpStatus.NOT_FOUND.value())
        Assertions.assertThat(problemDetail.detail).isEqualTo("Something is wrong")
    }

    @Test
    fun create_copiesOnlyExposedContextEntries() {

        val exception = ApplicationException(EntityErrorType.ENTITY_INVALID)
            .addContextValue("USERNAME", "john")
            .addContextValue("ENTITY", "secret-entity")

        val problemDetail = problemDetailFactory.create(exception, HttpStatus.BAD_REQUEST)

        Assertions.assertThat(problemDetail.properties)
            .containsEntry("USERNAME", "john")
            .doesNotContainKey("ENTITY")
    }

    @Test
    fun create_alwaysCopiesExceptionMessage() {

        val exception = ApplicationException(EntityErrorType.ENTITY_INVALID)
            .addContextValue(CommonErrorContext.EXCEPTION_MESSAGE, "ENTITY_INVALID")

        val problemDetail = ProblemDetailFactory().create(exception, HttpStatus.BAD_REQUEST)

        Assertions.assertThat(problemDetail.properties)
            .containsEntry(CommonErrorContext.EXCEPTION_MESSAGE.name, "ENTITY_INVALID")
    }

    @Test
    fun create_whenValidationException_listsErrorsWithoutRejectedValues() {

        val secret = "secret-password"
        val errors = BeanPropertyBindingResult(Credentials("john", secret), "credentials")
            .apply { rejectValue("password", "weak", "Password is too weak") }

        val problemDetail = problemDetailFactory.create(
            ValidationException(EntityErrorType.ENTITY_INVALID, errors),
            HttpStatus.BAD_REQUEST
        )

        Assertions.assertThat(problemDetail.properties)
            .containsEntry(
                ProblemDetailFactory.ERRORS_PROPERTY,
                listOf(mapOf("field" to "password", "code" to "weak", "message" to "Password is too weak"))
            )
            .doesNotContainKey("ERRORS")
        Assertions.assertThat(problemDetail.toString()).doesNotContain(secret)
    }
}
