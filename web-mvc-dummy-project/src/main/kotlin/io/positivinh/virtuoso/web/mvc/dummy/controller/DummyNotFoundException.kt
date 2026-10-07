package io.positivinh.virtuoso.web.mvc.dummy.controller

import com.crabshue.commons.exceptions.ApplicationException
import io.positivinh.virtuoso.domain.validations.exceptions.EntityErrorType

/**
 * A service-specific [ApplicationException] subclass, mapped to `404` by [DummyControllerAdvice].
 */
class DummyNotFoundException(username: String) : ApplicationException(EntityErrorType.ENTITY_INVALID, "Not found") {

    init {
        addContextValue("USERNAME", username)
    }
}
