package dev.devmonks.metrdotel.restaurants.controller


import dev.devmonks.metrdotel.restaurants.dto.RestaurantRequest
import dev.devmonks.metrdotel.restaurants.model.Restaurant
import dev.devmonks.metrdotel.restaurants.service.IRestaurantService
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
@RequestMapping("/restaurants")
@Api("Operations related to restaurants")
class RestaurantController @Autowired constructor(private val restaurantService: IRestaurantService) {

    private val logger = LoggerFactory.getLogger(RestaurantController::class.java)

    @Throws(Exception::class)
    @ApiOperation("Create a new restaurant")
    @ApiResponses(value = [
        ApiResponse(code = 201, message = "Returns new restaurant", response = Restaurant::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @PostMapping("")
    fun createRestaurant(@RequestBody payload: RestaurantRequest): ResponseEntity<*> {
        logger.debug("Create new restaurant request...")
        return ResponseEntity(this.restaurantService.createRestaurant(payload), HttpStatus.CREATED)
    }

    @Throws(Exception::class)
    @ApiOperation("Gets a list of all restaurants")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns list of all restaurants", response = Array<Restaurant>::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @GetMapping("")
    fun getAllRestaurants(): ResponseEntity<*> {
        logger.debug("Get all restaurants request...")
        return ResponseEntity(this.restaurantService.getAllRestaurants(), HttpStatus.OK)
    }

    @Throws(Exception::class)
    @ApiOperation("Get single restaurant")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns restaurant", response = Restaurant::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 404, message = "Restaurant not found"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @GetMapping("/{id}")
    fun getRestaurant(@PathVariable id: String): ResponseEntity<*> {
        logger.debug("Get restaurant request...")
        return ResponseEntity(this.restaurantService.getRestaurant(id), HttpStatus.OK)
    }

    @Throws(Exception::class)
    @ApiOperation("Update restaurant")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns whether restaurant was updated", response = Boolean::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 404, message = "Restaurant not found"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @PutMapping("/{id}")
    fun updateRestaurant(@PathVariable id: String, @RequestBody payload: RestaurantRequest): ResponseEntity<*> {
        logger.debug("Update restaurant request...")
        return ResponseEntity(this.restaurantService.updateRestaurant(id, payload), HttpStatus.OK)
    }

    @Throws(Exception::class)
    @ApiOperation("Add cover image")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns whether cover image was updated", response = Boolean::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 404, message = "Restaurant not found"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @PutMapping("/{id}/cover-image", consumes = ["multipart/form-data"])
    fun addProfilePicture(@PathVariable id: String, @RequestParam(value = "picture") pictureFile: MultipartFile): ResponseEntity<*> {
        return ResponseEntity(
                this.restaurantService.addCoverImage(
                        id,
                        pictureFile
                ),
                HttpStatus.OK
        )
    }

    @Throws(Exception::class)
    @ApiOperation("Delete restaurant")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns whether restaurant was deleted", response = Boolean::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 404, message = "Restaurant not found"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @DeleteMapping("/{id}")
    fun deleteRestaurant(@PathVariable id: String): ResponseEntity<*> {
        logger.debug("Delete restaurant request...")
        return ResponseEntity(this.restaurantService.deleteRestaurant(id), HttpStatus.OK)
    }


}