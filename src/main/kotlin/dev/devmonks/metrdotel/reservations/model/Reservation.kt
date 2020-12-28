package dev.devmonks.metrdotel.reservations.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class Reservation (
        val id: String,
        val restaurantId: String,
        val userId: String,
        val dateTime: LocalDateTime,
        val numberOfPeople: Int,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        val createdAt: LocalDateTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        val updatedAt: LocalDateTime,
        )