package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.annotation.Id
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import java.util.*

@SpringBootApplication
class DemoApplication

@JvmInline
value class Identifier<out T>(val value: UUID = UUID.randomUUID()) {
    override fun toString(): String = value.toString()
}

@Table("car")
data class Car(
	@Id val id: Identifier<Car>,
	val name: String,
)

interface CarRepository : CrudRepository<Car, Identifier<Car>>

@Configuration
class JdbcConfig : AbstractJdbcConfiguration() {
    @WritingConverter
    object IdentifierToUuidConverter : Converter<Identifier<*>, UUID> {
        override fun convert(source: Identifier<*>): UUID {
            return source.value
        }
    }

    @ReadingConverter
    object UuidToIdentifierConverter : Converter<UUID, Identifier<*>> {
        override fun convert(source: UUID): Identifier<*> {
            return Identifier<Any>(source)
        }
    }

    override fun userConverters(): List<*> = listOf(
        IdentifierToUuidConverter, UuidToIdentifierConverter
    )
}

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}
