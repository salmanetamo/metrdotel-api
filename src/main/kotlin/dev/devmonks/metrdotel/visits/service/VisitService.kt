package dev.devmonks.metrdotel.visits.service

import dev.devmonks.metrdotel.error.exception.EntityNotFoundException
import dev.devmonks.metrdotel.visits.dto.VisitRequest
import dev.devmonks.metrdotel.visits.model.Visit
import dev.devmonks.metrdotel.visits.repository.IVisitRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class VisitService @Autowired constructor(private val visitRepository: IVisitRepository): IVisitService {
    private val logger = LoggerFactory.getLogger(VisitService::class.java)

    override fun getAllVisits(): List<Visit> {
        logger.info("[VisitService] Fetching all visits...")
        return this.visitRepository.getAllVisits()
    }

    override fun getAllVisitsForRestaurant(restaurantId: String): List<Visit> {
        logger.info("[VisitService] Fetching visits for restaurant...")
        return this.visitRepository.getAllVisitsForRestaurant(restaurantId)
    }

    override fun getAllVisitsForUser(userId: String): List<Visit> {
        logger.info("[VisitService] Fetching visits for user...")
        return this.visitRepository.getAllVisitsForUser(userId)
    }

    override fun createVisit(visitRequest: VisitRequest, restaurantId: String, userId: String): Visit {
        logger.info("[VisitService] Creating visit...")
        visitRequest.id = UUID.randomUUID().toString()
        // TODO: Check if restaurant and user exist
        visitRequest.restaurantId = restaurantId
        visitRequest.userId = userId

        return this.visitRepository.createVisit(visitRequest.toVisit())
    }

    override fun getVisit(id: String): Visit {
        logger.info("[VisitService] Fetching visits...")
        val visit = this.visitRepository.getVisit(id)
        if (visit !== null) {
            return visit
        }
        throw EntityNotFoundException(Visit::class.java, "id", id)
    }

    override fun updateVisit(id: String, userId: String, visitRequest: VisitRequest): Boolean {
        logger.info("[VisitService] Updating visit...")
        val visit = this.getVisit(id)
        visitRequest.id = id
        visitRequest.userId = userId
        val newVisit = visitRequest.toVisit()
        return this.visitRepository.updateVisit(
                id,
                visit.copy(
                        reservationId = newVisit.reservationId,
                        updatedAt = newVisit.updatedAt
                )
        )
    }

    override fun deleteVisit(id: String): Boolean {
        logger.info("[VisitService] Deleting visit...")
        this.getVisit(id)
        return this.visitRepository.deleteVisit(id)
    }
}