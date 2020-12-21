package dev.devmonks.metrdotel.reviews.service

import dev.devmonks.metrdotel.reviews.dto.ReviewRequest
import dev.devmonks.metrdotel.reviews.model.Review

interface IReviewService {
    fun getAllReviews(): List<Review>
    fun getAllReviewsForRestaurant(restaurantId: String): List<Review>
    fun getAllReviewsByUser(userId: String): List<Review>
    fun createReview(reviewRequest: ReviewRequest, restaurantId: String, userId: String): Review
    fun getReview(id: String): Review
    fun updateReview(id: String, userId: String, reviewRequest: ReviewRequest): Boolean
    fun deleteReview(id: String): Boolean
}