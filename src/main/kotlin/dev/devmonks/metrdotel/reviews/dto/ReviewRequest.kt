package dev.devmonks.metrdotel.reviews.dto

import dev.devmonks.metrdotel.reviews.model.Review
import java.time.LocalDateTime

class ReviewRequest(
        var id: String?,
        var restaurantId: String?,
        var reviewerId: String?,
        var comment: String,
        var rating: Int
) {
    fun toReview(): Review {
        return Review(
                id = this.id?: "",
                restaurantId = this.restaurantId?: "",
                reviewerId = this.reviewerId?: "",
                comment = this.comment,
                rating = this.rating,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
        )
    }
}