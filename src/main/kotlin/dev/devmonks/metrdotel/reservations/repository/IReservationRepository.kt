package dev.devmonks.metrdotel.reservations.repository

import dev.devmonks.metrdotel.reservations.model.Reservation

interface IReservationRepository {
    fun getAllReservations(): List<Reservation>
    fun getAllReservationsForRestaurant(restaurantId: String): List<Reservation>
    fun getAllReservationsForUser(userId: String): List<Reservation>
    fun createReservation(reservation: Reservation): Reservation
    fun getReservation(id: String): Reservation?
    fun updateReservation(id: String, reservation: Reservation): Boolean
    fun deleteReservation(id: String): Boolean
}