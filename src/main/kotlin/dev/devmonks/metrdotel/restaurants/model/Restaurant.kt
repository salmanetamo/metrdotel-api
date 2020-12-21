package dev.devmonks.metrdotel.restaurants.model

import dev.devmonks.metrdotel.orders.model.Order
import dev.devmonks.metrdotel.reservations.model.Reservation
import dev.devmonks.metrdotel.reviews.model.Review
import java.time.LocalDateTime

data class Restaurant(
        val id: String,
        val amenities: List<Amenity>,
        val type: PlaceType,
        val coverImage: String,
        val openingHours: Map<String, List<Map<String, String>>>,
        val priceRange: Int,
        val name: String,
        val description: String,
        val reviews: List<Review>,
        val menu: List<MenuItem>,
        val orders: List<Order>,
        val reservations: List<Reservation>,
        val location: Location,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime,
)