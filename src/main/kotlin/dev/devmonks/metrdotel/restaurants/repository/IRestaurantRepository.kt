package dev.devmonks.metrdotel.restaurants.repository

import dev.devmonks.metrdotel.restaurants.model.Restaurant

interface IRestaurantRepository {
    fun getAllRestaurants(): List<Restaurant>
    fun createRestaurant(restaurant: Restaurant): Restaurant
    fun getRestaurant(id: String): Restaurant?
    fun updateRestaurant(id: String, restaurant: Restaurant): Boolean
    fun addCoverImage(id: String, fileName: String): Boolean
    fun deleteRestaurant(id: String): Boolean
}
