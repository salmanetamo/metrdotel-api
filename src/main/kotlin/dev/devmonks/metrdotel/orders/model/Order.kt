package dev.devmonks.metrdotel.orders.model

import java.time.LocalDateTime

data class Order (
        val id: String,
        val restaurantId: String,
        val userId: String,
        val items: List<OrderItem>,
        val discount: Double,
        val waiterTip: Double,
        val dateTime: LocalDateTime,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime,
)