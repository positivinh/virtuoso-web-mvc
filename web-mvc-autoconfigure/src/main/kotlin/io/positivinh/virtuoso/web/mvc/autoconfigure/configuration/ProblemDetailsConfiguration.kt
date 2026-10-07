package io.positivinh.virtuoso.web.mvc.autoconfigure.configuration

import io.positivinh.virtuoso.web.mvc.problem.ProblemDetailFactory
import io.positivinh.virtuoso.web.mvc.problem.VirtuosoProblemDetailAdvice
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class ProblemDetailsConfiguration {

    @Bean
    @ConditionalOnMissingBean
    fun problemDetailFactory(properties: ProblemDetailsConfigurationProperties): ProblemDetailFactory =
        ProblemDetailFactory(properties.exposedContextKeys)

    @Bean
    @ConditionalOnMissingBean
    fun virtuosoProblemDetailAdvice(problemDetailFactory: ProblemDetailFactory): VirtuosoProblemDetailAdvice =
        VirtuosoProblemDetailAdvice(problemDetailFactory)
}
