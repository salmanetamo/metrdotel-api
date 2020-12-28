package dev.devmonks.metrdotel.reviews.dto

import dev.devmonks.metrdotel.reviews.model.Review
import java.time.LocalDateTime
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

class ReviewRequest(
        var id: String?,
        var restaurantId: String?,
        var reviewerId: String?,
        var comment: String,
        @field:NotNull(message = "Rating cannot be null")
        @field:Min(message = "Rating cannot be less than ", value = 1)
        @field:Max(message = "Rating cannot be greater than", value = 5)
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