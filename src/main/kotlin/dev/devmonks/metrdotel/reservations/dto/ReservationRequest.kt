package dev.devmonks.metrdotel.reservations.dto

import dev.devmonks.metrdotel.error.validator.AfterNow
import dev.devmonks.metrdotel.error.validator.DateTimeFormat
import dev.devmonks.metrdotel.reservations.model.Reservation
import dev.devmonks.metrdotel.shared.Helper
import java.time.LocalDateTime
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class ReservationRequest(
        var id: String?,
        var restaurantId: String?,
        var userId: String?,
        @field:NotBlank(message = "Date time cannot be blank")
        @field:DateTimeFormat(message = "Invalid date time format")
        @field:AfterNow(message = "Date time must be after the current date and time")
        var dateTime: String,
        @field:NotNull(message = "Number of people cannot be null")
        @field:Min(message = "Number of people must be greater than 0", value = 1)
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