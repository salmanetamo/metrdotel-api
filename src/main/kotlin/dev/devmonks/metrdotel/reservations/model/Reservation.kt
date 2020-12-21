package dev.devmonks.metrdotel.reservations.model

import java.time.LocalDateTime

data class Reservation (
        val id: String,
        val restaurantId: String,
        val userId: String,
        val dateTime: LocalDateTime,
        val numberOfPeople: Int,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime
        )