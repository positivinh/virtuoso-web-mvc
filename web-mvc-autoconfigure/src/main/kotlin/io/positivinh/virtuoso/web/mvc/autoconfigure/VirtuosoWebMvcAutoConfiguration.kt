package io.positivinh.virtuoso.web.mvc.autoconfigure

import com.crabshue.commons.exceptions.ApplicationException
import io.positivinh.virtuoso.domain.validations.exceptions.ValidationException
import io.positivinh.virtuoso.web.mvc.autoconfigure.configuration.ProblemDetailsConfiguration
import io.positivinh.virtuoso.web.mvc.autoconfigure.configuration.ProblemDetailsConfigurationProperties
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Import
import org.springframework.http.ProblemDetail

/**
 * Answers application exceptions with RFC 7807 `ProblemDetail`s: a [io.positivinh.virtuoso.web.mvc.problem.ProblemDetailFactory]
 * for services' own advices, and a fallback advice mapping any `ApplicationException` to `400`.
 */
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass(ProblemDetail::class, ApplicationException::class, ValidationException::class)
@EnableConfigurationProperties(ProblemDetailsConfigurationProperties::class)
@Import(ProblemDetailsConfiguration::class)
class VirtuosoWebMvcAutoConfiguration
