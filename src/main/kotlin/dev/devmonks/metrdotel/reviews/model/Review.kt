package dev.devmonks.metrdotel.reviews.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class Review (
        val id: String,
        val restaurantId: String,
        val reviewerId: String,
        val comment: String,
        val rating: Int,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        val createdAt: LocalDateTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        val updatedAt: LocalDateTime,
        )