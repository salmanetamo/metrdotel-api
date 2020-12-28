package dev.devmonks.metrdotel.reviews.service

import dev.devmonks.metrdotel.error.exception.EntityNotFoundException
import dev.devmonks.metrdotel.reviews.dto.ReviewRequest
import dev.devmonks.metrdotel.reviews.model.Review
import dev.devmonks.metrdotel.reviews.repository.IReviewRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class ReviewService @Autowired constructor(private val reviewRepository: IReviewRepository): IReviewService {
    private val logger = LoggerFactory.getLogger(ReviewService::class.java)

    override fun getAllReviews(): List<Review> {
        logger.info("[ReviewService] Fetching all reviews...")
        return this.reviewRepository.getAllReviews()
    }

    override fun getAllReviewsForRestaurant(restaurantId: String): List<Review> {
        logger.info("[ReviewService] Fetching reviews for restaurant...")
        return this.reviewRepository.getAllReviewsForRestaurant(restaurantId)
    }

    override fun getAllReviewsByUser(userId: String): List<Review> {
        logger.info("[ReviewService] Fetching reviews for user...")
        return this.reviewRepository.getAllReviewsByUser(userId)
    }

    override fun createReview(reviewRequest: ReviewRequest, restaurantId: String, userId: String): Review {
        logger.info("[ReviewService] Creating review...")
        reviewRequest.id = UUID.randomUUID().toString()
        // TODO: Check if restaurant and user exist
        reviewRequest.restaurantId = restaurantId
        reviewRequest.reviewerId = userId

        return this.reviewRepository.createReview(reviewRequest.toReview())
    }

    override fun getReview(id: String): Review {
        logger.info("[ReviewService] Fetching reviews...")
        val review = this.reviewRepository.getReview(id)
        if (review !== null) {
            return review
        }
        throw EntityNotFoundException(Review::class.java, "id", id)
    }

    override fun updateReview(id: String, userId: String, reviewRequest: ReviewRequest): Boolean {
        logger.info("[ReviewService] Updating review...")
        val review = this.getReview(id)
        reviewRequest.id = id
        reviewRequest.reviewerId = userId
        val newReview = reviewRequest.toReview()
        return this.reviewRepository.updateReview(
                id,
                review.copy(
                        rating = newReview.rating,
                        comment = newReview.comment,
                        updatedAt = newReview.updatedAt
                )
        )
    }

    override fun deleteReview(id: String): Boolean {
        logger.info("[ReviewService] Deleting review...")
        this.getReview(id)
        return this.reviewRepository.deleteReview(id)
    }
}