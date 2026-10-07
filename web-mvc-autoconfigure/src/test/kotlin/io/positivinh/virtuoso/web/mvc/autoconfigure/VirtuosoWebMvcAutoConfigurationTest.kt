package io.positivinh.virtuoso.web.mvc.autoconfigure

import com.crabshue.commons.exceptions.ApplicationException
import io.positivinh.virtuoso.domain.validations.exceptions.EntityErrorType
import io.positivinh.virtuoso.web.mvc.problem.ProblemDetailFactory
import io.positivinh.virtuoso.web.mvc.problem.VirtuosoProblemDetailAdvice
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.AutoConfigurations
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import org.springframework.boot.test.context.runner.WebApplicationContextRunner
import org.springframework.http.HttpStatus

class VirtuosoWebMvcAutoConfigurationTest {

    private val webContextRunner = WebApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(VirtuosoWebMvcAutoConfiguration::class.java))

    @Test
    fun registersFactoryAndAdvice() {

        webContextRunner
            .withPropertyValues("virtuoso.web.mvc.problem-details.exposed-context-keys=USERNAME,OTHER_USERNAME")
            .run { context ->
                Assertions.assertThat(context).hasSingleBean(ProblemDetailFactory::class.java)
                Assertions.assertThat(context).hasSingleBean(VirtuosoProblemDetailAdvice::class.java)

                val exception = ApplicationException(EntityErrorType.ENTITY_INVALID)
                    .addContextValue("OTHER_USERNAME", "jane")
                val problemDetail = context.getBean(ProblemDetailFactory::class.java)
                    .create(exception, HttpStatus.BAD_REQUEST)
                Assertions.assertThat(problemDetail.properties).containsEntry("OTHER_USERNAME", "jane")
            }
    }

    @Test
    fun backsOffWhenFactoryIsDefined() {

        val customFactory = ProblemDetailFactory(setOf("CUSTOM"))

        webContextRunner
            .withBean(ProblemDetailFactory::class.java, { customFactory })
            .run { context ->
                Assertions.assertThat(context).hasSingleBean(ProblemDetailFactory::class.java)
                Assertions.assertThat(context.getBean(ProblemDetailFactory::class.java)).isSameAs(customFactory)
                Assertions.assertThat(context).hasSingleBean(VirtuosoProblemDetailAdvice::class.java)
            }
    }

    @Test
    fun notRegisteredOutsideServletApplications() {

        ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(VirtuosoWebMvcAutoConfiguration::class.java))
            .run { context ->
                Assertions.assertThat(context).doesNotHaveBean(ProblemDetailFactory::class.java)
                Assertions.assertThat(context).doesNotHaveBean(VirtuosoProblemDetailAdvice::class.java)
            }
    }
}
