package dev.devmonks.metrdotel.orders.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class Order (
        val id: String,
        val restaurantId: String,
        val userId: String,
        val items: List<OrderItem>,
        val discount: Double,
        val waiterTip: Double,
        val dateTime: LocalDateTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        val createdAt: LocalDateTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        val updatedAt: LocalDateTime,
)