package dev.devmonks.metrdotel.restaurants.dto

import dev.devmonks.metrdotel.restaurants.model.MenuItem
import java.time.LocalDateTime
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class MenuItemRequest(
        var id: String?,
        var restaurantId: String?,
        @field:NotBlank(message = "Name cannot be blank")
        var name: String,
        var picture: String,
        @field:NotNull(message = "Price cannot be null")
        @field:Min(message = "Price cannot be negative", value = 0)
        var price: Double,
        @field:NotBlank(message = "Name cannot be blank")
        var description: String,
        @field:Size(message = "Types cannot be empty", min = 1)
        @field:NotNull(message = "Types cannot be null")
        var types: List<String>
) {
    fun toMenuItem(): MenuItem {
        return MenuItem(
                id = this.id?: "",
                restaurantId = this.restaurantId?: "",
                name = this.name,
                picture = this.picture,
                price = this.price,
                description = this.description,
                types = this.types,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
        )
    }
}