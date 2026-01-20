package com.example.demo

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.test.assertEquals

@SpringBootTest
@Testcontainers
class CarRepositoryTests {

    companion object {
        @Container
        @ServiceConnection
        val postgres = PostgreSQLContainer("postgres:16-alpine")
    }

    @Autowired
    lateinit var carRepository: CarRepository

    @Test
    fun `findAllById should work with value classes`() {
        val car1 = Car(Identifier(), "Tesla")
        val car2 = Car(Identifier(), "BMW")
        val car3 = Car(Identifier(), "Audi")

        carRepository.save(car1)
        carRepository.save(car2)
        carRepository.save(car3)

        val ids = listOf(car1.id, car3.id)

        // This fails due to:
        // Caused by: org.postgresql.util.PSQLException: Can't infer the SQL type to use for an instance of com.example.demo.Identifier. Use setObject() with an explicit Types value to specify the type to use
        val result = carRepository.findAllById(ids).toList()

        assertEquals(2, result.size)
    }
}
