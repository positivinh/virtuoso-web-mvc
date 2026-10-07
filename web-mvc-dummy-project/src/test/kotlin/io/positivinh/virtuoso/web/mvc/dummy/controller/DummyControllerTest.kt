package io.positivinh.virtuoso.web.mvc.dummy.controller

import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
class DummyControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun applicationException_answeredBadRequest() {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/dummy/application-error"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("$.detail").value("Something is wrong"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.USERNAME").value(DummyController.USERNAME))
            .andExpect(MockMvcResultMatchers.jsonPath("$.ENTITY").doesNotExist())
            .andExpect(
                MockMvcResultMatchers.content().string(Matchers.not(Matchers.containsString(DummyController.SECRET)))
            )
    }

    @Test
    fun validationException_listsErrorsWithoutRejectedValues() {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/dummy/validation-error"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].field").value("password"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].code").value("weak"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.ERRORS").doesNotExist())
            .andExpect(
                MockMvcResultMatchers.content().string(Matchers.not(Matchers.containsString(DummyController.SECRET)))
            )
    }

    @Test
    fun serviceAdvice_takesPrecedenceOverFallback() {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/dummy/not-found"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.jsonPath("$.USERNAME").value(DummyController.USERNAME))
    }
}
