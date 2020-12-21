package dev.devmonks.metrdotel.restaurants.service

import dev.devmonks.metrdotel.restaurants.dto.MenuItemRequest
import dev.devmonks.metrdotel.restaurants.model.MenuItem
import org.springframework.web.multipart.MultipartFile

interface IMenuService {
    fun getAllMenuItems(): List<MenuItem>
    fun getAllMenuItemsForRestaurant(restaurantId: String): List<MenuItem>
    fun createMenuItem(menuItemRequest: MenuItemRequest, restaurantId: String): MenuItem
    fun getMenuItem(id: String): MenuItem
    fun updateMenuItem(id: String, menuItemRequest: MenuItemRequest): Boolean
    fun deleteMenuItem(id: String): Boolean
    fun addPicture(id: String, pictureFile: MultipartFile): Boolean
}