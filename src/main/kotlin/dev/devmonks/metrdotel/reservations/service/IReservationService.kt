package dev.devmonks.metrdotel.reservations.service

import dev.devmonks.metrdotel.reservations.dto.ReservationRequest
import dev.devmonks.metrdotel.reservations.model.Reservation

interface IReservationService {
    fun getAllReservations(): List<Reservation>
    fun getAllReservationsForRestaurant(restaurantId: String): List<Reservation>
    fun getAllReservationsForUser(userId: String): List<Reservation>
    fun createReservation(reservationRequest: ReservationRequest, restaurantId: String, userId: String): Reservation
    fun getReservation(id: String): Reservation
    fun updateReservation(id: String, userId: String, reservationRequest: ReservationRequest): Boolean
    fun deleteReservation(id: String): Boolean
}