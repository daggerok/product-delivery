package io.github.daggerok.productdelivery

import java.math.BigDecimal
import mu.KLogging
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import reactor.kotlin.test.test

class DeliveryRepositoryTest(@Autowired val deliveryRepository: DeliveryRepository) : AbstractApplicationTest() {

    @Test
    fun `should test if datalayer is working fine`() {
        // setup
        deliveryRepository.deleteAll()
            .test()
            .verifyComplete()
        // given
        deliveryRepository.save(Delivery(budget = BigDecimal("12.34"), notes = "Pick 2 pizzas and deliver to customer"))
            .test()
            .consumeNextWith { logger.info { it } }
            .verifyComplete()
    }

    companion object : KLogging()
}
