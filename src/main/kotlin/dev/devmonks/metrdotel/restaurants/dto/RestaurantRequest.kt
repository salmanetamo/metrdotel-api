package dev.devmonks.metrdotel.restaurants.dto

import dev.devmonks.metrdotel.restaurants.model.Amenity
import dev.devmonks.metrdotel.restaurants.model.Location
import dev.devmonks.metrdotel.restaurants.model.PlaceType
import dev.devmonks.metrdotel.restaurants.model.Restaurant
import java.time.LocalDateTime
import javax.validation.constraints.*

class RestaurantRequest(
        var id: String?,
        @field:Size(message = "Amenities cannot be empty", min = 1)
        @field:NotNull(message = "Amenities cannot be null")
        var amenities: List<String>,
        @field:NotBlank(message = "Type cannot be blank")
        var type: String,
        var coverImage: String,
        var openingHours: Map<String, List<Map<String, String>>>,
        @field:NotNull(message = "Price range cannot be null")
        @field:Min(message = "Price range cannot be less than", value = 1)
        @field:Max(message = "Price range cannot be greater than", value = 3)
        var priceRange: Int,
        @field:NotBlank(message = "Name cannot be blank")
        var name: String,
        @field:NotBlank(message = "Description cannot be blank")
        var description: String,
        @field:NotBlank(message = "Location name cannot be blank")
        var locationName: String,
        @field:NotNull(message = "Longitude cannot be null")
        @field:Min(message = "Longitude cannot be less than -", value = 180)
        @field:Max(message = "Longitude cannot be greater than", value = 180)
        var longitude: Double,
        @field:NotNull(message = "Latitude cannot be null")
        @field:Min(message = "Latitude cannot be less than -", value = 90)
        @field:Max(message = "Latitude cannot be greater than", value = 90)
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
