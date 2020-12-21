package dev.devmonks.metrdotel.restaurants.controller


import dev.devmonks.metrdotel.restaurants.dto.MenuItemRequest
import dev.devmonks.metrdotel.restaurants.model.MenuItem
import dev.devmonks.metrdotel.restaurants.service.IMenuService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@CrossOrigin(origins = ["*"])
@RestController
@RequestMapping("/menus")
@Api("Operations related to restaurant menus")
class MenuController @Autowired constructor(private val menuService: IMenuService) {

    private val logger = LoggerFactory.getLogger(MenuController::class.java)

    @Throws(Exception::class)
    @ApiOperation("Create a new menu item")
    @ApiResponses(value = [
        ApiResponse(code = 201, message = "Returns new restaurant", response = MenuItem::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @PostMapping("/{restaurantId}")
    fun createMenuItem(@RequestBody payload: MenuItemRequest, @PathVariable restaurantId: String): ResponseEntity<*> {
        logger.info("Create new menu item request...")
        return ResponseEntity(this.menuService.createMenuItem(payload, restaurantId), HttpStatus.CREATED)
    }

    @Throws(Exception::class)
    @ApiOperation("Gets a list of all menu items")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns list of all menu items", response = Array<MenuItem>::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @GetMapping("")
    fun getAllMenuItems(): ResponseEntity<*> {
        logger.info("Get all menu items request...")
        return ResponseEntity(this.menuService.getAllMenuItems(), HttpStatus.OK)
    }

    @Throws(Exception::class)
    @ApiOperation("Gets a list of all menu items for restaurant")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns list of all menu items for restaurant", response = Array<MenuItem>::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @GetMapping("/restaurant/{restaurantId}")
    fun getAllMenuItemsForRestaurant(@PathVariable restaurantId: String): ResponseEntity<*> {
        logger.info("Get all menu items for restaurant request...")
        return ResponseEntity(this.menuService.getAllMenuItemsForRestaurant(restaurantId), HttpStatus.OK)
    }

    @Throws(Exception::class)
    @ApiOperation("Get single menu item")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns menu item", response = MenuItem::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 404, message = "Menu item not found"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @GetMapping("/{id}")
    fun getMenuItem(@PathVariable id: String): ResponseEntity<*> {
        logger.info("Get menu item request...")
        return ResponseEntity(this.menuService.getMenuItem(id), HttpStatus.OK)
    }

    @Throws(Exception::class)
    @ApiOperation("Update menu item")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns whether menu item was updated", response = Boolean::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 404, message = "Menu item not found"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @PutMapping("/{id}")
    fun updateMenuItem(@PathVariable id: String, @RequestBody payload: MenuItemRequest): ResponseEntity<*> {
        logger.info("Update menu item request...")
        return ResponseEntity(this.menuService.updateMenuItem(id, payload), HttpStatus.OK)
    }


    @Throws(Exception::class)
    @ApiOperation("Add picture")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns whether picture was updated", response = Boolean::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 404, message = "Menu item not found"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @PutMapping("/{id}/picture", consumes = ["multipart/form-data"])
    fun addProfilePicture(@PathVariable id: String, @RequestParam(value = "picture") pictureFile: MultipartFile): ResponseEntity<*> {
        return ResponseEntity(
                this.menuService.addPicture(
                        id,
                        pictureFile
                ),
                HttpStatus.OK
        )
    }


    @Throws(Exception::class)
    @ApiOperation("Delete menu item")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns whether menu item was deleted", response = Boolean::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 404, message = "Menu item not found"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @DeleteMapping("/{id}")
    fun deleteMenuItem(@PathVariable id: String): ResponseEntity<*> {
        logger.info("Delete menu item request...")
        return ResponseEntity(this.menuService.deleteMenuItem(id), HttpStatus.OK)
    }

}