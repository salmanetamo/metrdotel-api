package dev.devmonks.metrdotel.restaurants.dto

import dev.devmonks.metrdotel.restaurants.model.MenuItem
import java.time.LocalDateTime

class MenuItemRequest(
        var id: String?,
        var restaurantId: String?,
        var name: String,
        var picture: String,
        var price: Double,
        var description: String,
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