package io.positivinh.virtuoso.web.mvc.dummy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DummyWebMvcApplication

fun main(args: Array<String>) {

    runApplication<DummyWebMvcApplication>(*args)
}
