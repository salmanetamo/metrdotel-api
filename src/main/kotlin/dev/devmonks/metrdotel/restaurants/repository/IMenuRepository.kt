package dev.devmonks.metrdotel.restaurants.repository

import dev.devmonks.metrdotel.restaurants.model.MenuItem

interface IMenuRepository {
    fun getAllMenuItems(): List<MenuItem>
    fun getAllMenuItemsForRestaurant(restaurantId: String): List<MenuItem>
    fun createMenuItem(menuItem: MenuItem): MenuItem
    fun getMenuItem(id: String): MenuItem?
    fun updateMenuItem(id: String, menuItem: MenuItem): Boolean
    fun deleteMenuItem(id: String): Boolean
    fun addPicture(id: String, fileName: String): Boolean
}