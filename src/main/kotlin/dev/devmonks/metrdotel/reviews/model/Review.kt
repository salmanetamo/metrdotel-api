package dev.devmonks.metrdotel.reviews.model

import java.time.LocalDateTime

data class Review (
        val id: String,
        val restaurantId: String,
        val reviewerId: String,
        val comment: String,
        val rating: Int,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime
        )