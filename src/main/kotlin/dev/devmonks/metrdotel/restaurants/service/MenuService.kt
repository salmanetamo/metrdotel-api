package dev.devmonks.metrdotel.restaurants.service

import dev.devmonks.metrdotel.error.exception.EntityNotFoundException
import dev.devmonks.metrdotel.restaurants.dto.MenuItemRequest
import dev.devmonks.metrdotel.restaurants.model.MenuItem
import dev.devmonks.metrdotel.restaurants.repository.IMenuRepository
import dev.devmonks.metrdotel.shared.filestorage.FileStorageConstants
import dev.devmonks.metrdotel.shared.filestorage.service.FileStorageService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.util.*

@Component
class MenuItemService @Autowired constructor(
        private val menuRepository: IMenuRepository,
        private val fileStorageService: FileStorageService
): IMenuService {

    private val logger = LoggerFactory.getLogger(MenuItemService::class.java)

    override fun getAllMenuItems(): List<MenuItem> {
        logger.info("[MenuItemService] Fetching menuItems...")
        return this.menuRepository.getAllMenuItems()
    }

    override fun getAllMenuItemsForRestaurant(restaurantId: String): List<MenuItem> {
        logger.info("[MenuItemService] Fetching menuItems...")
        return this.menuRepository.getAllMenuItems()
    }

    override fun createMenuItem(menuItemRequest: MenuItemRequest, restaurantId: String): MenuItem {
        logger.info("[MenuItemService] Creating menuItem...")
        menuItemRequest.id = UUID.randomUUID().toString()
        // TODO: Check if restaurant exists
        menuItemRequest.restaurantId = restaurantId
        return this.menuRepository.createMenuItem(menuItemRequest.toMenuItem())
    }

    override fun getMenuItem(id: String): MenuItem {
        logger.info("[MenuItemService] Fetching menuItem...")
        val menuItem = this.menuRepository.getMenuItem(id)
        if (menuItem !== null) {
            return menuItem
        }
        throw EntityNotFoundException(MenuItem::class.java, id)
    }

    override fun updateMenuItem(id: String, menuItemRequest: MenuItemRequest): Boolean {
        logger.info("[MenuItemService] Updating menuItem...")
        val menuItem = this.getMenuItem(id)
        val newMenuItem = menuItemRequest.toMenuItem()
        return this.menuRepository.updateMenuItem(
                id,
                menuItem.copy(
                        name = newMenuItem.name,
                        picture = newMenuItem.picture,
                        price = newMenuItem.price,
                        description = newMenuItem.description,
                        types = newMenuItem.types,
                        updatedAt = newMenuItem.updatedAt
                )
        )
    }

    override fun addPicture(id: String, pictureFile: MultipartFile): Boolean {
        this.getMenuItem(id)
        val fileName: String = this.fileStorageService.storeFile(
                pictureFile,
                FileStorageConstants.MENU_ITEM_PIC
        )
        return this.menuRepository.addPicture(id, fileName)
    }

    override fun deleteMenuItem(id: String): Boolean {
        logger.info("[MenuItemService] Deleting menuItem...")
        this.getMenuItem(id)
        return this.menuRepository.deleteMenuItem(id)
    }
}