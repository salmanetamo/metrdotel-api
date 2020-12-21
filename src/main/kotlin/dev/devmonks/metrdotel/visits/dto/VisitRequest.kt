package dev.devmonks.metrdotel.visits.dto

import dev.devmonks.metrdotel.visits.model.Visit
import java.time.LocalDateTime

class VisitRequest(
        var id: String?,
        var restaurantId: String?,
        var userId: String?,
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