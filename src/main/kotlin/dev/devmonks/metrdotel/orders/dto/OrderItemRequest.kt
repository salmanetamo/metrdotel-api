package dev.devmonks.metrdotel.orders.dto

import dev.devmonks.metrdotel.orders.model.OrderItem
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class OrderItemRequest(
        var id: String?,
        @field:NotBlank(message = "Menu item id cannot be blank")
        var menuItemId: String,
        var orderId: String?,
        @field:NotNull(message = "Quantity cannot be null")
        @field:Min(message = "Quantity must be greater than 0", value = 1)
        var quantity: Int
) {
    fun toOrderItem(): OrderItem {
        return OrderItem(
                id = this.id ?: "",
                orderId = this.orderId ?: "",
                menuItemId = this.menuItemId,
                quantity = this.quantity
        )
    }
}