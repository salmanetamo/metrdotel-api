package dev.devmonks.metrdotel.reservations.controller


import dev.devmonks.metrdotel.reservations.dto.ReservationRequest
import dev.devmonks.metrdotel.reservations.model.Reservation
import dev.devmonks.metrdotel.reservations.service.IReservationService
import dev.devmonks.metrdotel.users.service.IUserService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["*"])
@RestController
@RequestMapping("/reservations")
@Api("Operations related to reservations")
class ReservationController @Autowired constructor(
        private val reservationService: IReservationService,
        private val userService: IUserService) {

    private val logger = LoggerFactory.getLogger(ReservationController::class.java)

    @Throws(Exception::class)
    @ApiOperation("Create a new reservation")
    @ApiResponses(value = [
        ApiResponse(code = 201, message = "Returns new reservation", response = Reservation::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @PostMapping("/{restaurantId}")
    fun createReservation(@RequestBody payload: ReservationRequest, @PathVariable restaurantId: String): ResponseEntity<*> {
        logger.debug("Create new reservation request...")
        return ResponseEntity(this.reservationService.createReservation(
                payload,
                restaurantId,
                this.userService.getUserByEmail(
                        SecurityContextHolder.getContext().authentication.principal.toString()
                ).id
        ),
                HttpStatus.CREATED
        )
    }

    @Throws(Exception::class)
    @ApiOperation("Gets a list of all reservations")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns list of all reservations", response = Array<Reservation>::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @GetMapping("")
    fun getAllReservations(): ResponseEntity<*> {
        logger.debug("Get all reservations request...")
        return ResponseEntity(this.reservationService.getAllReservations(), HttpStatus.OK)
    }

    @Throws(Exception::class)
    @ApiOperation("Gets a list of all reservations")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns list of all reservations for restaurant", response = Array<Reservation>::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @GetMapping("/restaurant/{restaurantId}")
    fun getAllReservationsForRestaurant(@PathVariable restaurantId: String): ResponseEntity<*> {
        logger.debug("Get all reservations request...")
        return ResponseEntity(this.reservationService.getAllReservationsForRestaurant(restaurantId), HttpStatus.OK)
    }

    @Throws(Exception::class)
    @ApiOperation("Gets a list of all reservations")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns list of all reservations for restaurant", response = Array<Reservation>::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @GetMapping("/user/{userId}")
    fun getAllReservationsForUser(@PathVariable userId: String): ResponseEntity<*> {
        logger.debug("Get all reservations request...")
        return ResponseEntity(this.reservationService.getAllReservationsForUser(userId), HttpStatus.OK)
    }

    @Throws(Exception::class)
    @ApiOperation("Get single reservation")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns reservation", response = Reservation::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 404, message = "Reservation not found"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @GetMapping("/{id}")
    fun getReservation(@PathVariable id: String): ResponseEntity<*> {
        logger.debug("Get reservation request...")
        return ResponseEntity(this.reservationService.getReservation(id), HttpStatus.OK)
    }

    @Throws(Exception::class)
    @ApiOperation("Update reservation")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns whether reservation was updated", response = Boolean::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 404, message = "Reservation not found"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @PutMapping("/{id}")
    fun updateReservation(@PathVariable id: String, @RequestBody payload: ReservationRequest): ResponseEntity<*> {
        logger.debug("Update reservation request...")
        return ResponseEntity(
                this.reservationService.updateReservation(
                        id,
                        this.userService.getUserByEmail(
                                SecurityContextHolder.getContext().authentication.principal.toString()
                        ).id,
                        payload
                ),
                HttpStatus.OK
        )
    }

    @Throws(Exception::class)
    @ApiOperation("Delete reservation")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns whether reservation was deleted", response = Boolean::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 404, message = "Reservation not found"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @DeleteMapping("/{id}")
    fun deleteReservation(@PathVariable id: String): ResponseEntity<*> {
        logger.debug("Delete reservation request...")
        return ResponseEntity(this.reservationService.deleteReservation(id), HttpStatus.OK)
    }


}