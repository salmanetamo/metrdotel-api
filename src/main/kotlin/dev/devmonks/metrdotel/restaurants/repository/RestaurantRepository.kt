package dev.devmonks.metrdotel.restaurants.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dev.devmonks.metrdotel.restaurants.model.*
import dev.devmonks.metrdotel.users.repository.usersIdColumn
import dev.devmonks.metrdotel.users.repository.usersProfilePictureColumn
import dev.devmonks.metrdotel.users.repository.usersTable
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Component
import java.sql.ResultSet
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.sql.DataSource

const val restaurantsTable = "restaurants"

const val restaurantIdColumn = "id"
const val restaurantNameColumn = "name"
const val restaurantTypeColumn = "type"
const val restaurantCoverImageColumn = "cover_image"
const val restaurantOpeningHoursColumn = "opening_hours"
const val restaurantAmenitiesColumn = "amenities"
const val restaurantPriceRangeColumn = "price_range"
const val restaurantDescriptionColumn = "description"
const val restaurantLocationColumn = "location"
const val restaurantCreatedAtColumn = "created_at"
const val restaurantUpdatedAtColumn = "updated_at"

@Component
class RestaurantRepository @Autowired constructor(private val dataSource: DataSource): IRestaurantRepository {

    private val logger = LoggerFactory.getLogger(RestaurantRepository::class.java)

    private final val mapper: ObjectMapper

    init {
        val javaTimeModule = JavaTimeModule()
        javaTimeModule.addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
        this.mapper = jacksonObjectMapper().registerModule(javaTimeModule).findAndRegisterModules()
    }

    override fun getAllRestaurants(): List<Restaurant> {
        logger.info("[RestaurantRepository] Fetching all restaurants...")
        val query: String =
                """SELECT
                    | $restaurantIdColumn,
                    | $restaurantNameColumn,
                    | $restaurantTypeColumn,
                    | $restaurantAmenitiesColumn,
                    | $restaurantCoverImageColumn,
                    | $restaurantOpeningHoursColumn,
                    | $restaurantPriceRangeColumn,
                    | $restaurantDescriptionColumn,
                    | $restaurantLocationColumn,
                    | $restaurantCreatedAtColumn,
                    | $restaurantUpdatedAtColumn
                    | FROM $restaurantsTable
                """.trimMargin()

        return JdbcTemplate(dataSource).query(query){
            rs: ResultSet, _: Int -> resultSetToRestaurant(rs)
        }
    }

    override fun createRestaurant(restaurant: Restaurant): Restaurant {
        logger.info("[RestaurantRepository] Creating restaurant...")
        val insert = SimpleJdbcInsert(this.dataSource)
                .withTableName(restaurantsTable)
                .usingColumns(
                        restaurantIdColumn,
                        restaurantNameColumn,
                        restaurantTypeColumn,
                        restaurantCoverImageColumn,
                        restaurantOpeningHoursColumn,
                        restaurantAmenitiesColumn,
                        restaurantPriceRangeColumn,
                        restaurantDescriptionColumn,
                        restaurantLocationColumn,
                        restaurantCreatedAtColumn,
                        restaurantUpdatedAtColumn,
                )

        val parameters = HashMap<String, Any>(1)
        parameters[restaurantIdColumn] = restaurant.id
        parameters[restaurantNameColumn] = restaurant.name
        parameters[restaurantTypeColumn] = restaurant.type.value
        parameters[restaurantCoverImageColumn] = restaurant.coverImage
        parameters[restaurantOpeningHoursColumn] = mapper.writeValueAsString(restaurant.openingHours)
        parameters[restaurantAmenitiesColumn] = restaurant.amenities.map { it.value }.toTypedArray()
        parameters[restaurantPriceRangeColumn] = restaurant.priceRange
        parameters[restaurantDescriptionColumn] = restaurant.description
        parameters[restaurantLocationColumn] = mapper.writeValueAsString(restaurant.location)
        parameters[restaurantCreatedAtColumn] = restaurant.createdAt
        parameters[restaurantUpdatedAtColumn] = restaurant.updatedAt

        insert.execute(parameters)
        return restaurant
    }

    override fun getRestaurant(id: String): Restaurant? {
        logger.info("[RestaurantRepository] Fetching restaurant...")
        val query: String =
                """SELECT
                    | $restaurantIdColumn,
                    | $restaurantNameColumn,
                    | $restaurantTypeColumn,
                    | $restaurantCoverImageColumn,
                    | $restaurantAmenitiesColumn,
                    | $restaurantOpeningHoursColumn,
                    | $restaurantPriceRangeColumn,
                    | $restaurantDescriptionColumn,
                    | $restaurantLocationColumn,
                    | $restaurantCreatedAtColumn,
                    | $restaurantUpdatedAtColumn
                    | FROM $restaurantsTable
                    | WHERE id = ?
                """.trimMargin()

        val restaurantList = JdbcTemplate(dataSource).query(query, arrayOf(id)){
            rs: ResultSet, _: Int -> resultSetToRestaurant(rs)
        }

        return if (restaurantList.isEmpty()) {
            null
        } else {
            restaurantList.first()
        }
    }

    override fun updateRestaurant(id: String, restaurant: Restaurant): Boolean {
        logger.info("[RestaurantRepository] Updating restaurant...")
        val query: String =
                """UPDATE $restaurantsTable
                    | SET $restaurantNameColumn = ?,
                    | $restaurantTypeColumn = ?,
                    | $restaurantCoverImageColumn = ?,
                    | $restaurantOpeningHoursColumn = CAST(? AS JSON),
                    | $restaurantAmenitiesColumn = ?,
                    | $restaurantPriceRangeColumn = ?,
                    | $restaurantDescriptionColumn = ?,
                    | $restaurantLocationColumn = CAST(? AS JSON),
                    | $restaurantUpdatedAtColumn = ?
                    | WHERE $restaurantIdColumn = ?
                """.trimMargin()

        JdbcTemplate(this.dataSource)
                .update(
                        query,
                        ArgumentPreparedStatementSetter(
                                arrayOf(
                                        restaurant.name,
                                        restaurant.type.value,
                                        restaurant.coverImage,
                                        mapper.writeValueAsString(restaurant.openingHours),
                                        restaurant.amenities.map { it.value }.toTypedArray(),
                                        restaurant.priceRange,
                                        restaurant.description,
                                        mapper.writeValueAsString(restaurant.location),
                                        restaurant.createdAt,
                                        id
                                )
                        )
                )

        return true
    }

    override fun addCoverImage(id: String, fileName: String): Boolean {
        val query: String =
                """UPDATE $restaurantsTable
                    | SET $restaurantCoverImageColumn = ?
                    | WHERE $restaurantIdColumn = ?
                """.trimMargin()

        JdbcTemplate(this.dataSource)
                .update(
                        query,
                        ArgumentPreparedStatementSetter(
                                arrayOf(
                                        fileName,
                                        id
                                )
                        )
                )

        return true
    }

    override fun deleteRestaurant(id: String): Boolean {
        logger.info("[RestaurantRepository] Deleting restaurant...")
        val query: String =
                """DELETE FROM $restaurantsTable
                    | WHERE $restaurantIdColumn = ?
                """.trimMargin()

        JdbcTemplate(this.dataSource).update(query, ArgumentPreparedStatementSetter(arrayOf(id)))
        return true
    }

    private fun resultSetToRestaurant(resultSet: ResultSet): Restaurant {
        return Restaurant(
                id = resultSet.getString(restaurantIdColumn),
                amenities = (resultSet.getArray(restaurantAmenitiesColumn).array as Array<String>)
                        .map { Amenity.fromString(it) },
                type = PlaceType.fromString(resultSet.getString(restaurantTypeColumn)),
                openingHours = mapper.readValue(resultSet.getString(restaurantOpeningHoursColumn)),
                coverImage = resultSet.getString(restaurantCoverImageColumn),
                priceRange = resultSet.getInt(restaurantPriceRangeColumn),
                name = resultSet.getString(restaurantNameColumn),
                description = resultSet.getString(restaurantDescriptionColumn),
                location = mapper.readValue(resultSet.getString(restaurantLocationColumn)),
                reviews = listOf(),
                menu = listOf(),
                orders = listOf(),
                reservations = listOf(),
                createdAt = resultSet.getTimestamp(restaurantCreatedAtColumn).toLocalDateTime(),
                updatedAt = resultSet.getTimestamp(restaurantUpdatedAtColumn).toLocalDateTime(),
        )
    }
}