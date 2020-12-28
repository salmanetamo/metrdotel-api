package dev.devmonks.metrdotel.restaurants.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class MenuItem (
        val id: String,
        val restaurantId: String,
        val name: String,
        val picture: String,
        val price: Double,
        val description: String,
        val types: List<String>,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        val createdAt: LocalDateTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        val updatedAt: LocalDateTime,
        )