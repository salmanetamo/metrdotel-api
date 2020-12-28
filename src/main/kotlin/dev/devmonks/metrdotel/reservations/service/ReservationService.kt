package dev.devmonks.metrdotel.reservations.service

import dev.devmonks.metrdotel.error.exception.EntityNotFoundException
import dev.devmonks.metrdotel.reservations.dto.ReservationRequest
import dev.devmonks.metrdotel.reservations.model.Reservation
import dev.devmonks.metrdotel.reservations.repository.IReservationRepository
import dev.devmonks.metrdotel.shared.filestorage.FileStorageConstants
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Component
class ReservationService @Autowired constructor(private val reservationRepository: IReservationRepository): IReservationService {
    private val logger = LoggerFactory.getLogger(ReservationService::class.java)

    override fun getAllReservations(): List<Reservation> {
        logger.info("[ReservationService] Fetching all reservations...")
        return this.reservationRepository.getAllReservations()
    }

    override fun getAllReservationsForRestaurant(restaurantId: String): List<Reservation> {
        logger.info("[ReservationService] Fetching reservations for restaurant...")
        return this.reservationRepository.getAllReservationsForRestaurant(restaurantId)
    }

    override fun getAllReservationsForUser(userId: String): List<Reservation> {
        logger.info("[ReservationService] Fetching reservations for user...")
        return this.reservationRepository.getAllReservationsForUser(userId)
    }

    override fun createReservation(reservationRequest: ReservationRequest, restaurantId: String, userId: String): Reservation {
        logger.info("[ReservationService] Creating reservation...")
        reservationRequest.id = UUID.randomUUID().toString()
        // TODO: Check if restaurant and user exist
        reservationRequest.restaurantId = restaurantId
        reservationRequest.userId = userId

        return this.reservationRepository.createReservation(reservationRequest.toReservation())
    }

    override fun getReservation(id: String): Reservation {
        logger.info("[ReservationService] Fetching reservations...")
        val reservation = this.reservationRepository.getReservation(id)
        if (reservation !== null) {
            return reservation
        }
        throw EntityNotFoundException(Reservation::class.java, "id", id)
    }

    override fun updateReservation(id: String, userId: String, reservationRequest: ReservationRequest): Boolean {
        logger.info("[ReservationService] Updating reservation...")
        val reservation = this.getReservation(id)
        reservationRequest.id = id
        reservationRequest.userId = userId
        val newReservation = reservationRequest.toReservation()
        return this.reservationRepository.updateReservation(
                id,
                reservation.copy(
                        numberOfPeople = newReservation.numberOfPeople,
                        dateTime = newReservation.dateTime,
                        updatedAt = newReservation.updatedAt
                )
        )
    }

    override fun deleteReservation(id: String): Boolean {
        logger.info("[ReservationService] Deleting reservation...")
        this.getReservation(id)
        return this.reservationRepository.deleteReservation(id)
    }
}