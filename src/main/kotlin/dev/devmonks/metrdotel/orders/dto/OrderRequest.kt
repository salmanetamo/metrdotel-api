package dev.devmonks.metrdotel.orders.dto

import dev.devmonks.metrdotel.orders.model.Order
import dev.devmonks.metrdotel.shared.Helper
import java.time.LocalDateTime

class OrderRequest(
        var id: String?,
        var restaurantId: String?,
        var userId: String?,
        var items: List<OrderItemRequest>,
        var discount: Double,
        var waiterTip: Double,
        var dateTime: String,
) {
    fun toOrder(): Order {
        return Order(
                id = this.id?: "",
                restaurantId = this.restaurantId?: "",
                userId = this.userId?: "",
                items = this.items.map { it.toOrderItem() },
                discount = this.discount,
                waiterTip = this.waiterTip,
                dateTime = Helper.getDateTimeFromString(this.dateTime),
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
        )
    }
}