package dev.devmonks.metrdotel.orders.dto

import dev.devmonks.metrdotel.orders.model.OrderItem

class OrderItemRequest(
        var id: String?,
        var menuItemId: String,
        var orderId: String?,
        var quantity: Int
) {
    fun toOrderItem(): OrderItem {
        return OrderItem(
                id = this.id?: "",
                orderId = this.orderId?: "",
                menuItemId = this.menuItemId,
                quantity = this.quantity
        )
    }
}