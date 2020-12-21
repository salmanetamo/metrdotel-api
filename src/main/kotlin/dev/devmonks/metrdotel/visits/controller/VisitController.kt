package dev.devmonks.metrdotel.visits.controller


import dev.devmonks.metrdotel.visits.dto.VisitRequest
import dev.devmonks.metrdotel.visits.model.Visit
import dev.devmonks.metrdotel.visits.service.IVisitService
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
@RequestMapping("/visits")
@Api("Operations related to visits")
class VisitController @Autowired constructor(
        private val visitService: IVisitService,
        private val userService: IUserService) {

    private val logger = LoggerFactory.getLogger(VisitController::class.java)

    @Throws(Exception::class)
    @ApiOperation("Create a new visit")
    @ApiResponses(value = [
        ApiResponse(code = 201, message = "Returns new visit", response = Visit::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @PostMapping("/{restaurantId}")
    fun createVisit(@RequestBody payload: VisitRequest, @PathVariable restaurantId: String): ResponseEntity<*> {
        logger.debug("Create new visit request...")
        return ResponseEntity(this.visitService.createVisit(
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
    @ApiOperation("Gets a list of all visits")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns list of all visits", response = Array<Visit>::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @GetMapping("")
    fun getAllVisits(): ResponseEntity<*> {
        logger.debug("Get all visits request...")
        return ResponseEntity(this.visitService.getAllVisits(), HttpStatus.OK)
    }

    @Throws(Exception::class)
    @ApiOperation("Gets a list of all visits")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns list of all visits for restaurant", response = Array<Visit>::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @GetMapping("/restaurant/{restaurantId}")
    fun getAllVisitsForRestaurant(@PathVariable restaurantId: String): ResponseEntity<*> {
        logger.debug("Get all visits request...")
        return ResponseEntity(this.visitService.getAllVisitsForRestaurant(restaurantId), HttpStatus.OK)
    }

    @Throws(Exception::class)
    @ApiOperation("Gets a list of all visits")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns list of all visits for restaurant", response = Array<Visit>::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @GetMapping("/user/{userId}")
    fun getAllVisitsForUser(@PathVariable userId: String): ResponseEntity<*> {
        logger.debug("Get all visits request...")
        return ResponseEntity(this.visitService.getAllVisitsForUser(userId), HttpStatus.OK)
    }

    @Throws(Exception::class)
    @ApiOperation("Get single visit")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns visit", response = Visit::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 404, message = "Visit not found"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @GetMapping("/{id}")
    fun getVisit(@PathVariable id: String): ResponseEntity<*> {
        logger.debug("Get visit request...")
        return ResponseEntity(this.visitService.getVisit(id), HttpStatus.OK)
    }

    @Throws(Exception::class)
    @ApiOperation("Update visit")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns whether visit was updated", response = Boolean::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 404, message = "Visit not found"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @PutMapping("/{id}")
    fun updateVisit(@PathVariable id: String, @RequestBody payload: VisitRequest): ResponseEntity<*> {
        logger.debug("Update visit request...")
        return ResponseEntity(
                this.visitService.updateVisit(
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
    @ApiOperation("Delete visit")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns whether visit was deleted", response = Boolean::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 404, message = "Visit not found"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @DeleteMapping("/{id}")
    fun deleteVisit(@PathVariable id: String): ResponseEntity<*> {
        logger.debug("Delete visit request...")
        return ResponseEntity(this.visitService.deleteVisit(id), HttpStatus.OK)
    }
}