package io.github.daggerok.productdelivery

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@SpringBootTest(webEnvironment = RANDOM_PORT)
abstract class AbstractApplicationTest {

    companion object {

        data class TestMySQLContainer(val image: String = "mysql:8.0.30") : MySQLContainer<TestMySQLContainer>(image)

        @Container
        val mysqlContainer: TestMySQLContainer = TestMySQLContainer()

        @JvmStatic
        @DynamicPropertySource
        fun patchConfigProps(registry: DynamicPropertyRegistry) {
            registry.add("spring.r2dbc.url") { mysqlContainer.jdbcUrl.replaceFirst("jdbc:", "r2dbc:") }
            registry.add("spring.r2dbc.username", mysqlContainer::getUsername)
            registry.add("spring.r2dbc.password", mysqlContainer::getPassword)
        }
    }
}
