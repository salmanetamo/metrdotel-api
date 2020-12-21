package dev.devmonks.metrdotel.reservations.dto

import dev.devmonks.metrdotel.reservations.model.Reservation
import dev.devmonks.metrdotel.shared.Helper
import java.time.LocalDateTime

class ReservationRequest(
        var id: String?,
        var restaurantId: String?,
        var userId: String?,
        var dateTime: String,
        var numberOfPeople: Int
) {
    fun toReservation(): Reservation {
        return Reservation(
                id = this.id?:"",
                restaurantId = this.restaurantId?:"",
                userId = this.userId?:"",
                dateTime = Helper.getDateTimeFromString(this.dateTime),
                numberOfPeople = this.numberOfPeople,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
        )
    }
}