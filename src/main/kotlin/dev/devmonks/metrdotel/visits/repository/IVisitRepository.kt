package dev.devmonks.metrdotel.visits.repository

import dev.devmonks.metrdotel.visits.model.Visit

interface IVisitRepository {
    fun getAllVisits(): List<Visit>
    fun getAllVisitsForRestaurant(restaurantId: String): List<Visit>
    fun getAllVisitsForUser(userId: String): List<Visit>
    fun createVisit(visit: Visit): Visit
    fun getVisit(id: String): Visit?
    fun updateVisit(id: String, visit: Visit): Boolean
    fun deleteVisit(id: String): Boolean
}