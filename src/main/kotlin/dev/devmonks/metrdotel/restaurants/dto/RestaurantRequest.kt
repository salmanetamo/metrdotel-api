package dev.devmonks.metrdotel.restaurants.dto

import dev.devmonks.metrdotel.restaurants.model.Amenity
import dev.devmonks.metrdotel.restaurants.model.Location
import dev.devmonks.metrdotel.restaurants.model.PlaceType
import dev.devmonks.metrdotel.restaurants.model.Restaurant
import java.time.LocalDateTime

class RestaurantRequest(
        var id: String?,
        var amenities: List<String>,
        var type: String,
        var coverImage: String,
        var openingHours: Map<String, List<Map<String, String>>>,
        var priceRange: Int,
        var name: String,
        var description: String,
        var locationName: String,
        var longitude: Double,
        var latitude: Double
) {
    fun toRestaurant(): Restaurant {
        return Restaurant(
                id = this.id?: "",
                amenities = this.amenities.map{amenity -> Amenity.fromString(amenity)},
                type = PlaceType.fromString(this.type),
                coverImage = this.coverImage,
                priceRange = this.priceRange,
                name = this.name,
                location = Location(this.locationName, this.longitude, this.latitude),
                description = this.description,
                openingHours = this.openingHours,
                reviews = listOf(),
                orders = listOf(),
                reservations = listOf(),
                menu = listOf(),
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
        )
    }
}
