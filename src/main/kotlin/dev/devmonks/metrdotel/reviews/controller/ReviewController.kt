package dev.devmonks.metrdotel.reviews.controller


import dev.devmonks.metrdotel.reviews.dto.ReviewRequest
import dev.devmonks.metrdotel.reviews.model.Review
import dev.devmonks.metrdotel.reviews.service.IReviewService
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
@RequestMapping("/reviews")
@Api("Operations related to reviews")
class ReviewController @Autowired constructor(
        private val reviewService: IReviewService,
        private val userService: IUserService) {

    private val logger = LoggerFactory.getLogger(ReviewController::class.java)

    @Throws(Exception::class)
    @ApiOperation("Create a new review")
    @ApiResponses(value = [
        ApiResponse(code = 201, message = "Returns new review", response = Review::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @PostMapping("/{restaurantId}")
    fun createReview(@RequestBody payload: ReviewRequest, @PathVariable restaurantId: String): ResponseEntity<*> {
        logger.debug("Create new review request...")
        return ResponseEntity(this.reviewService.createReview(
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
    @ApiOperation("Gets a list of all reviews")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns list of all reviews", response = Array<Review>::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @GetMapping("")
    fun getAllReviews(): ResponseEntity<*> {
        logger.debug("Get all reviews request...")
        return ResponseEntity(this.reviewService.getAllReviews(), HttpStatus.OK)
    }

    @Throws(Exception::class)
    @ApiOperation("Gets a list of all reviews for restaurant")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns list of all reviews for restaurant", response = Array<Review>::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @GetMapping("/restaurant/{restaurantId}")
    fun getAllReviewsForRestaurant(@PathVariable restaurantId: String): ResponseEntity<*> {
        logger.debug("Get all reviews request...")
        return ResponseEntity(this.reviewService.getAllReviewsForRestaurant(restaurantId), HttpStatus.OK)
    }

    @Throws(Exception::class)
    @ApiOperation("Gets a list of all reviews by user")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns list of all reviews by user", response = Array<Review>::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @GetMapping("/user/{userId}")
    fun getAllReviewsByUser(@PathVariable userId: String): ResponseEntity<*> {
        logger.debug("Get all reviews request...")
        return ResponseEntity(this.reviewService.getAllReviewsByUser(userId), HttpStatus.OK)
    }

    @Throws(Exception::class)
    @ApiOperation("Get single review")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns review", response = Review::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 404, message = "Review not found"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @GetMapping("/{id}")
    fun getReview(@PathVariable id: String): ResponseEntity<*> {
        logger.debug("Get review request...")
        return ResponseEntity(this.reviewService.getReview(id), HttpStatus.OK)
    }

    @Throws(Exception::class)
    @ApiOperation("Update review")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns whether review was updated", response = Boolean::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 404, message = "Review not found"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @PutMapping("/{id}")
    fun updateReview(@PathVariable id: String, @RequestBody payload: ReviewRequest): ResponseEntity<*> {
        logger.debug("Update review request...")
        return ResponseEntity(
                this.reviewService.updateReview(
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
    @ApiOperation("Delete review")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns whether review was deleted", response = Boolean::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 404, message = "Review not found"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @DeleteMapping("/{id}")
    fun deleteReview(@PathVariable id: String): ResponseEntity<*> {
        logger.debug("Delete review request...")
        return ResponseEntity(this.reviewService.deleteReview(id), HttpStatus.OK)
    }


}