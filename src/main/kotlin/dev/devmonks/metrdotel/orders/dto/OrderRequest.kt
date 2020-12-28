package dev.devmonks.metrdotel.orders.dto

import dev.devmonks.metrdotel.error.validator.AfterNow
import dev.devmonks.metrdotel.error.validator.DateTimeFormat
import dev.devmonks.metrdotel.orders.model.Order
import dev.devmonks.metrdotel.shared.Helper
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class OrderRequest(
        var id: String?,
        var restaurantId: String?,
        var userId: String?,
        @field:Size(message = "Items cannot be empty", min = 1)
        @field:NotNull(message = "Items cannot be null")
        var items: List<OrderItemRequest>,
        @field:NotNull(message = "Discount cannot be null")
        var discount: Double,
        @field:NotNull(message = "Waiter tip cannot be null")
        var waiterTip: Double,
        @field:NotBlank(message = "Date time cannot be blank")
        @field:DateTimeFormat(message = "Invalid date time format")
        @field:AfterNow(message = "Date time must be after the current date and time")
        var dateTime: String,
) {
    fun toOrder(): Order {
        return Order(
                id = this.id ?: "",
                restaurantId = this.restaurantId ?: "",
                userId = this.userId ?: "",
                items = this.items.map { it.toOrderItem() },
                discount = this.discount,
                waiterTip = this.waiterTip,
                dateTime = Helper.getDateTimeFromString(this.dateTime),
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
        )
    }
}