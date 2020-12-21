package dev.devmonks.metrdotel.restaurants.service

import dev.devmonks.metrdotel.restaurants.dto.RestaurantRequest
import dev.devmonks.metrdotel.restaurants.model.Restaurant
import org.springframework.web.multipart.MultipartFile

interface IRestaurantService {
    fun getAllRestaurants(): List<Restaurant>
    fun createRestaurant(restaurantRequest: RestaurantRequest): Restaurant
    fun getRestaurant(id: String): Restaurant
    fun updateRestaurant(id: String, restaurantRequest: RestaurantRequest): Boolean
    fun addCoverImage(id: String, pictureFile: MultipartFile): Boolean
    fun deleteRestaurant(id: String): Boolean
}
