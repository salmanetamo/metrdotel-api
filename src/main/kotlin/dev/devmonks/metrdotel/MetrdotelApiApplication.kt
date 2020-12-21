package dev.devmonks.metrdotel

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MetrdotelApiApplication

fun main(args: Array<String>) {
    runApplication<MetrdotelApiApplication>(*args)
}
