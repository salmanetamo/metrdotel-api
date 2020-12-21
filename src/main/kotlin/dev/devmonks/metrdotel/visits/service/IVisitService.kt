package dev.devmonks.metrdotel.visits.service

import dev.devmonks.metrdotel.visits.dto.VisitRequest
import dev.devmonks.metrdotel.visits.model.Visit

interface IVisitService {
    fun getAllVisits(): List<Visit>
    fun getAllVisitsForRestaurant(restaurantId: String): List<Visit>
    fun getAllVisitsForUser(userId: String): List<Visit>
    fun createVisit(visitRequest: VisitRequest, restaurantId: String, userId: String): Visit
    fun getVisit(id: String): Visit
    fun updateVisit(id: String, userId: String, visitRequest: VisitRequest): Boolean
    fun deleteVisit(id: String): Boolean
}