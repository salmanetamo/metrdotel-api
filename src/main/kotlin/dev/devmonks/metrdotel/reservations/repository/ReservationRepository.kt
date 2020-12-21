package dev.devmonks.metrdotel.reservations.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import dev.devmonks.metrdotel.reservations.model.Reservation
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Component
import java.sql.ResultSet
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.sql.DataSource

const val reservationsTable = "reservations"
const val reservationIdColumn = "id"
const val reservationRestaurantIdColumn = "restaurant_id"
const val reservationUserIdColumn = "user_id"
const val reservationNumberOfPeopleColumn = "number_of_people"
const val reservationDatetimeColumn = "datetime"
const val reservationCreatedAtColumn = "created_at"
const val reservationUpdatedAtColumn = "updated_at"

@Component
class ReservationRepository constructor(private val dataSource: DataSource): IReservationRepository {
    private val logger = LoggerFactory.getLogger(ReservationRepository::class.java)
    private final val mapper: ObjectMapper

    init {
        val javaTimeModule = JavaTimeModule()
        javaTimeModule.addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
        this.mapper = jacksonObjectMapper().registerModule(javaTimeModule).findAndRegisterModules()
    }

    override fun getAllReservations(): List<Reservation> {
        logger.info("[ReservationRepository] Fetching all reservations...")
        val query: String =
                """SELECT
                    | $reservationIdColumn,
                    | $reservationRestaurantIdColumn,
                    | $reservationUserIdColumn, 
                    | $reservationNumberOfPeopleColumn, 
                    | $reservationDatetimeColumn, 
                    | $reservationCreatedAtColumn,
                    | $reservationUpdatedAtColumn
                    | FROM $reservationsTable
                """.trimMargin()

        return JdbcTemplate(dataSource).query(query){
            rs: ResultSet, _: Int -> resultSetToReservation(rs)
        }
    }

    override fun getAllReservationsForRestaurant(restaurantId: String): List<Reservation> {
        logger.info("[ReservationRepository] Fetching all reservations for restaurant...")
        val query: String =
                """SELECT
                    | $reservationIdColumn,
                    | $reservationRestaurantIdColumn,
                    | $reservationUserIdColumn, 
                    | $reservationNumberOfPeopleColumn, 
                    | $reservationDatetimeColumn, 
                    | $reservationCreatedAtColumn,
                    | $reservationUpdatedAtColumn
                    | FROM $reservationsTable
                    | WHERE $reservationRestaurantIdColumn = ?
                """.trimMargin()

        return JdbcTemplate(dataSource).query(query, arrayOf(restaurantId)){
            rs: ResultSet, _: Int -> resultSetToReservation(rs)
        }
    }

    override fun getAllReservationsForUser(userId: String): List<Reservation> {
        logger.info("[ReservationRepository] Fetching all reservations for user...")
        val query: String =
                """SELECT
                    | $reservationIdColumn,
                    | $reservationRestaurantIdColumn,
                    | $reservationUserIdColumn, 
                    | $reservationNumberOfPeopleColumn, 
                    | $reservationDatetimeColumn, 
                    | $reservationCreatedAtColumn,
                    | $reservationUpdatedAtColumn
                    | FROM $reservationsTable
                    | WHERE $reservationUserIdColumn = ?
                """.trimMargin()

        return JdbcTemplate(dataSource).query(query, arrayOf(userId)){
            rs: ResultSet, _: Int -> resultSetToReservation(rs)
        }
    }

    override fun createReservation(reservation: Reservation): Reservation {
        logger.info("[ReservationRepository] Creating reservation...")
        val insert = SimpleJdbcInsert(this.dataSource)
                .withTableName(reservationsTable)
                .usingColumns(
                        reservationIdColumn,
                        reservationRestaurantIdColumn,
                        reservationUserIdColumn,
                        reservationNumberOfPeopleColumn,
                        reservationDatetimeColumn,
                        reservationCreatedAtColumn,
                        reservationUpdatedAtColumn
                )
        val parameters = HashMap<String, Any>(1)
        parameters[reservationIdColumn] = reservation.id
        parameters[reservationRestaurantIdColumn] = reservation.restaurantId
        parameters[reservationUserIdColumn] = reservation.userId
        parameters[reservationNumberOfPeopleColumn] = reservation.numberOfPeople
        parameters[reservationDatetimeColumn] = reservation.dateTime
        parameters[reservationCreatedAtColumn] = reservation.createdAt
        parameters[reservationUpdatedAtColumn] = reservation.updatedAt

        insert.execute(parameters)

        return reservation
    }

    override fun getReservation(id: String): Reservation? {
        logger.info("[ReservationRepository] Fetching reservation...")
        val query: String =
                """SELECT
                    | $reservationIdColumn,
                    | $reservationRestaurantIdColumn,
                    | $reservationUserIdColumn, 
                    | $reservationNumberOfPeopleColumn, 
                    | $reservationDatetimeColumn, 
                    | $reservationCreatedAtColumn,
                    | $reservationUpdatedAtColumn
                    | FROM $reservationsTable
                    | WHERE $reservationIdColumn = ?
                """.trimMargin()

        val reservationsList = JdbcTemplate(dataSource).query(query, arrayOf(id)){
            rs: ResultSet, _: Int -> resultSetToReservation(rs)
        }

        return if (reservationsList.isEmpty()) {
            null
        } else {
            reservationsList.first()
        }
    }

    override fun updateReservation(id: String, reservation: Reservation): Boolean {
        logger.info("[ReservationRepository] Updating reservation...")
        val query: String =
                """UPDATE $reservationsTable
                    | SET $reservationRestaurantIdColumn = ?,
                    | $reservationUserIdColumn = ?,
                    | $reservationNumberOfPeopleColumn = ?,
                    | $reservationDatetimeColumn = ?,
                    | $reservationUpdatedAtColumn = ?
                    | WHERE $reservationIdColumn = ?
                """.trimMargin()

        JdbcTemplate(this.dataSource)
                .update(
                        query,
                        ArgumentPreparedStatementSetter(
                                arrayOf(
                                        reservation.restaurantId,
                                        reservation.userId,
                                        reservation.numberOfPeople,
                                        reservation.dateTime,
                                        reservation.updatedAt,
                                        id
                                )
                        )
                )

        return true
    }

    override fun deleteReservation(id: String): Boolean {
        logger.info("[ReservationRepository] Deleting reservation...")
        val query: String =
                """DELETE FROM $reservationsTable
                    | WHERE $reservationIdColumn = ?
                """.trimMargin()

        JdbcTemplate(this.dataSource).update(query, ArgumentPreparedStatementSetter(arrayOf(id)))
        return true
    }

    private fun resultSetToReservation(resultSet: ResultSet): Reservation {
        return Reservation(
                id = resultSet.getString(reservationIdColumn),
                restaurantId = resultSet.getString(reservationRestaurantIdColumn),
                userId = resultSet.getString(reservationUserIdColumn),
                numberOfPeople = resultSet.getInt(reservationNumberOfPeopleColumn),
                dateTime = resultSet.getTimestamp(reservationDatetimeColumn).toLocalDateTime(),
                createdAt = resultSet.getTimestamp(reservationCreatedAtColumn).toLocalDateTime(),
                updatedAt = resultSet.getTimestamp(reservationUpdatedAtColumn).toLocalDateTime(),
        )
    }
}