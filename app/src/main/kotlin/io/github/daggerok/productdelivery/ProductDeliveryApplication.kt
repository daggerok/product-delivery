package io.github.daggerok.productdelivery

import java.math.BigDecimal
import java.time.Instant
import java.time.Month
import java.time.ZoneOffset
import java.util.Locale
import java.util.TimeZone
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.data.relational.core.mapping.Table
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@SpringBootApplication
class ProductDeliveryApplication

fun main(args: Array<String>) {
    Locale.setDefault(Locale.US)
    TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC))
    runApplication<ProductDeliveryApplication>(*args)
}

enum class State {
    NONE, CREATED, CANCELLED
}

@Table("deliveries")
data class Delivery(

    val notes: String = "",
    val budget: BigDecimal = BigDecimal.ZERO,

    val state: State = State.NONE,

    @Version
    val version: Long? = null,

    @LastModifiedDate
    @DateTimeFormat(iso = DATE_TIME)
    val lastModifiedAt: Instant? = null,

    @Id val id: Long? = null,
)

interface DeliveryRepository : R2dbcRepository<Delivery, Long>

@Transactional
@RestController
class DeliveryResource(private val deliveryRepository: DeliveryRepository) {

    @PostMapping("/api/v1/delivery")
    fun createDelivery(@RequestBody delivery: Delivery) =
        deliveryRepository.save(delivery)
            .mapNotNull(Delivery::id)
            .map { Mono.justOrEmpty(it) }
            .flatMap(deliveryRepository::findById)

    @GetMapping("/api/v1/delivery")
    fun getAllDeliveries() =
        deliveryRepository.findAll()

    @PutMapping("/api/v1/delivery/{id}/{state}")
    fun updateDeliveryState(@PathVariable id: Long, @PathVariable state: String) =
        deliveryRepository.findById(id)
            .map { it.copy(state = State.valueOf(state.uppercase())) }
            .flatMap(deliveryRepository::save)
            .mapNotNull(Delivery::id)
            .map { Mono.justOrEmpty(it) }
            .flatMap(deliveryRepository::findById)
}
