package io.positivinh.virtuoso.web.mvc.dummy

import io.positivinh.virtuoso.web.mvc.problem.ProblemDetailFactory
import io.positivinh.virtuoso.web.mvc.problem.VirtuosoProblemDetailAdvice
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext

@SpringBootTest
class DummyWebMvcApplicationTest {

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @Test
    fun contextLoads() {

        Assertions.assertThat(applicationContext.getBeansOfType(ProblemDetailFactory::class.java)).hasSize(1)
        Assertions.assertThat(applicationContext.getBeansOfType(VirtuosoProblemDetailAdvice::class.java)).hasSize(1)
    }
}
