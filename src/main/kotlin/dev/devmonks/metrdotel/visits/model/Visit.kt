package dev.devmonks.metrdotel.visits.model

import java.time.LocalDateTime

data class Visit (
        val id: String,
        val restaurantId: String,
        val userId: String,
        val reservationId: String,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime
        )