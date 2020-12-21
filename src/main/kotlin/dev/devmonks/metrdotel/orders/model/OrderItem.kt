package dev.devmonks.metrdotel.orders.model

data class OrderItem (
        val id: String,
        val menuItemId: String,
        val orderId: String,
        val quantity: Int
)