package dev.devmonks.metrdotel.visits.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import dev.devmonks.metrdotel.visits.model.Visit
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Component
import java.sql.ResultSet
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.sql.DataSource

const val visitsTable = "visits"

const val visitIdColumn = "id"
const val visitRestaurantIdColumn = "restaurant_id"
const val visitUserIdColumn = "user_id"
const val visitReservationIdColumn = "reservation_id"
const val visitCreatedAtColumn = "created_at"
const val visitUpdatedAtColumn = "updated_at"

@Component
class VisitRepository constructor(private val dataSource: DataSource): IVisitRepository {
    private val logger = LoggerFactory.getLogger(VisitRepository::class.java)
    private final val mapper: ObjectMapper

    init {
        val javaTimeModule = JavaTimeModule()
        javaTimeModule.addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
        this.mapper = jacksonObjectMapper().registerModule(javaTimeModule).findAndRegisterModules()
    }

    override fun getAllVisits(): List<Visit> {
        logger.info("[VisitRepository] Fetching all visits...")
        val query: String =
                """SELECT
                    | $visitIdColumn,
                    | $visitRestaurantIdColumn,
                    | $visitUserIdColumn, 
                    | $visitReservationIdColumn, 
                    | $visitCreatedAtColumn,
                    | $visitUpdatedAtColumn
                    | FROM $visitsTable
                """.trimMargin()

        return JdbcTemplate(dataSource).query(query){
            rs: ResultSet, _: Int -> resultSetToVisit(rs)
        }
    }

    override fun getAllVisitsForRestaurant(restaurantId: String): List<Visit> {
        logger.info("[VisitRepository] Fetching all visits for restaurant...")
        val query: String =
                """SELECT
                    | $visitIdColumn,
                    | $visitRestaurantIdColumn,
                    | $visitUserIdColumn, 
                    | $visitReservationIdColumn, 
                    | $visitCreatedAtColumn,
                    | $visitUpdatedAtColumn
                    | FROM $visitsTable
                    | WHERE $visitRestaurantIdColumn = ?
                """.trimMargin()

        return JdbcTemplate(dataSource).query(query, arrayOf(restaurantId)){
            rs: ResultSet, _: Int -> resultSetToVisit(rs)
        }
    }

    override fun getAllVisitsForUser(userId: String): List<Visit> {
        logger.info("[VisitRepository] Fetching all visits for user...")
        val query: String =
                """SELECT
                    | $visitIdColumn,
                    | $visitRestaurantIdColumn,
                    | $visitUserIdColumn, 
                    | $visitReservationIdColumn, 
                    | $visitCreatedAtColumn,
                    | $visitUpdatedAtColumn
                    | FROM $visitsTable
                    | WHERE $visitUserIdColumn = ?
                """.trimMargin()

        return JdbcTemplate(dataSource).query(query, arrayOf(userId)){
            rs: ResultSet, _: Int -> resultSetToVisit(rs)
        }
    }

    override fun createVisit(visit: Visit): Visit {
        logger.info("[VisitRepository] Creating visit...")
        val insert = SimpleJdbcInsert(this.dataSource)
                .withTableName(visitsTable)
                .usingColumns(
                        visitIdColumn,
                        visitRestaurantIdColumn,
                        visitUserIdColumn,
                        visitReservationIdColumn,
                        visitCreatedAtColumn,
                        visitUpdatedAtColumn
                )
        val parameters = HashMap<String, Any>(1)
        parameters[visitIdColumn] = visit.id
        parameters[visitRestaurantIdColumn] = visit.restaurantId
        parameters[visitUserIdColumn] = visit.userId
        parameters[visitReservationIdColumn] = visit.reservationId
        parameters[visitCreatedAtColumn] = visit.createdAt
        parameters[visitUpdatedAtColumn] = visit.updatedAt

        insert.execute(parameters)

        return visit
    }

    override fun getVisit(id: String): Visit? {
        logger.info("[VisitRepository] Fetching visit...")
        val query: String =
                """SELECT
                    | $visitIdColumn,
                    | $visitRestaurantIdColumn,
                    | $visitUserIdColumn, 
                    | $visitReservationIdColumn, 
                    | $visitCreatedAtColumn,
                    | $visitUpdatedAtColumn
                    | FROM $visitsTable
                    | WHERE $visitIdColumn = ?
                """.trimMargin()

        val visitsList = JdbcTemplate(dataSource).query(query, arrayOf(id)){
            rs: ResultSet, _: Int -> resultSetToVisit(rs)
        }

        return if (visitsList.isEmpty()) {
            null
        } else {
            visitsList.first()
        }
    }

    override fun updateVisit(id: String, visit: Visit): Boolean {
        logger.info("[VisitRepository] Updating visit...")
        val query: String =
                """UPDATE $visitsTable
                    | SET $visitRestaurantIdColumn = ?,
                    | $visitUserIdColumn = ?,
                    | $visitReservationIdColumn = ?,
                    | $visitUpdatedAtColumn = ?
                    | WHERE $visitIdColumn = ?
                """.trimMargin()

        JdbcTemplate(this.dataSource)
                .update(
                        query,
                        ArgumentPreparedStatementSetter(
                                arrayOf(
                                        visit.restaurantId,
                                        visit.userId,
                                        visit.reservationId,
                                        visit.updatedAt,
                                        id
                                )
                        )
                )

        return true
    }

    override fun deleteVisit(id: String): Boolean {
        logger.info("[VisitRepository] Deleting visit...")
        val query: String =
                """DELETE FROM $visitsTable
                    | WHERE $visitIdColumn = ?
                """.trimMargin()

        JdbcTemplate(this.dataSource).update(query, ArgumentPreparedStatementSetter(arrayOf(id)))
        return true
    }

    private fun resultSetToVisit(resultSet: ResultSet): Visit {
        return Visit(
                id = resultSet.getString(visitIdColumn),
                restaurantId = resultSet.getString(visitRestaurantIdColumn),
                userId = resultSet.getString(visitUserIdColumn),
                reservationId = resultSet.getString(visitReservationIdColumn),
                createdAt = resultSet.getTimestamp(visitCreatedAtColumn).toLocalDateTime(),
                updatedAt = resultSet.getTimestamp(visitUpdatedAtColumn).toLocalDateTime(),
        )
    }
}