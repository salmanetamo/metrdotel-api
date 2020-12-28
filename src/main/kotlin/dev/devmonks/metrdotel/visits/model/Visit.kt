package dev.devmonks.metrdotel.visits.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class Visit (
        val id: String,
        val restaurantId: String,
        val userId: String,
        val reservationId: String,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        val createdAt: LocalDateTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        val updatedAt: LocalDateTime
        )