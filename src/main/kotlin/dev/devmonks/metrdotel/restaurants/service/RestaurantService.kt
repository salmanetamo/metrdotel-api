package dev.devmonks.metrdotel.restaurants.service

import dev.devmonks.metrdotel.error.exception.EntityNotFoundException
import dev.devmonks.metrdotel.restaurants.dto.RestaurantRequest
import dev.devmonks.metrdotel.restaurants.model.Restaurant
import dev.devmonks.metrdotel.restaurants.repository.IRestaurantRepository
import dev.devmonks.metrdotel.shared.filestorage.FileStorageConstants
import dev.devmonks.metrdotel.shared.filestorage.service.FileStorageService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Component
class RestaurantService @Autowired constructor(
            private val restaurantRepository: IRestaurantRepository,
            private val fileStorageService: FileStorageService
        ): IRestaurantService {

    private val logger = LoggerFactory.getLogger(RestaurantService::class.java)

    override fun getAllRestaurants(): List<Restaurant> {
        logger.info("[RestaurantService] Fetching restaurants...")
        return this.restaurantRepository.getAllRestaurants()
    }

    override fun createRestaurant(restaurantRequest: RestaurantRequest): Restaurant {
        logger.info("[RestaurantService] Creating restaurant...")
        restaurantRequest.id = UUID.randomUUID().toString()
        return this.restaurantRepository.createRestaurant(restaurantRequest.toRestaurant())
    }

    override fun getRestaurant(id: String): Restaurant {
        logger.info("[RestaurantService] Fetching restaurant...")
        val restaurant = this.restaurantRepository.getRestaurant(id)
        if (restaurant !== null) {
            return restaurant
        }
        throw EntityNotFoundException(Restaurant::class.java, "id", id)
    }

    override fun updateRestaurant(id: String, restaurantRequest: RestaurantRequest): Boolean {
        logger.info("[RestaurantService] Updating restaurant...")
        val restaurant = this.getRestaurant(id)
        val newRestaurant = restaurantRequest.toRestaurant()
        return this.restaurantRepository.updateRestaurant(
                id,
                restaurant.copy(
                        amenities = newRestaurant.amenities,
                        type = newRestaurant.type,
                        coverImage = newRestaurant.coverImage,
                        openingHours = newRestaurant.openingHours,
                        priceRange = newRestaurant.priceRange,
                        name = newRestaurant.name,
                        description = newRestaurant.description,
                        reviews = newRestaurant.reviews,
                        menu = newRestaurant.menu,
                        orders = newRestaurant.orders,
                        reservations = newRestaurant.reservations,
                        location = newRestaurant.location,
                        updatedAt = newRestaurant.updatedAt
                )
        )
    }

    override fun addCoverImage(id: String, pictureFile: MultipartFile): Boolean {
        this.getRestaurant(id)
        val fileName: String = this.fileStorageService.storeFile(
                pictureFile,
                FileStorageConstants.RESTAURANT_COVER_IMAGE
        )
        return this.restaurantRepository.addCoverImage(id, fileName)
    }

    override fun deleteRestaurant(id: String): Boolean {
        logger.info("[RestaurantService] Deleting restaurant...")
        this.getRestaurant(id)
        return this.restaurantRepository.deleteRestaurant(id)
    }
}