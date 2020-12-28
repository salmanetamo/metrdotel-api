package dev.devmonks.metrdotel.visits.dto

import dev.devmonks.metrdotel.visits.model.Visit
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank

class VisitRequest(
        var id: String?,
        var restaurantId: String?,
        var userId: String?,
        @field:NotBlank(message = "Reservation id cannot be blank")
        var reservationId: String?,
) {
    fun toVisit(): Visit {
        return Visit(
                id = this.id?: "",
                restaurantId = this.restaurantId?: "",
                userId = this.userId?: "",
                reservationId = this.reservationId?: "",
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
        )
    }
}