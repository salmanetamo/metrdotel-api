package dev.devmonks.metrdotel.restaurants.model

import java.time.LocalDateTime

data class MenuItem (
        val id: String,
        val restaurantId: String,
        val name: String,
        val picture: String,
        val price: Double,
        val description: String,
        val types: List<String>,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime,
        )