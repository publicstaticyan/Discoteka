package discoteka.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule

object Mapper {
    val objectMapper: ObjectMapper = ObjectMapper().registerModule(kotlinModule())
}