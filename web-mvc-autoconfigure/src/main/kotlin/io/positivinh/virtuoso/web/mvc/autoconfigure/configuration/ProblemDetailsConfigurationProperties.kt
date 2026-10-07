package io.positivinh.virtuoso.web.mvc.autoconfigure.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "virtuoso.web.mvc.problem-details")
data class ProblemDetailsConfigurationProperties(

    /**
     * Names of the exception context entries copied into error responses, in addition to `EXCEPTION_MESSAGE`, which
     * is always copied. Only list entries that are safe to show to the caller (e.g. `USERNAME`).
     */
    val exposedContextKeys: Set<String> = emptySet(),
)
