package dev.devmonks.metrdotel.reviews.repository

import dev.devmonks.metrdotel.reviews.model.Review

interface IReviewRepository {
    fun getAllReviews(): List<Review>
    fun getAllReviewsForRestaurant(restaurantId: String): List<Review>
    fun getAllReviewsByUser(userId: String): List<Review>
    fun createReview(review: Review): Review
    fun getReview(id: String): Review?
    fun updateReview(id: String, review: Review): Boolean
    fun deleteReview(id: String): Boolean
}