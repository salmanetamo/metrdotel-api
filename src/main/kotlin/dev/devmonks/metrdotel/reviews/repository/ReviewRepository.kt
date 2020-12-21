package dev.devmonks.metrdotel.reviews.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import dev.devmonks.metrdotel.reviews.model.Review
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Component
import java.sql.ResultSet
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.sql.DataSource

const val reviewsTable = "reviews"

const val reviewIdColumn = "id"
const val reviewRestaurantIdColumn = "restaurant_id"
const val reviewReviewerIdColumn = "reviewer_id"
const val reviewRatingColumn = "rating"
const val reviewCommentColumn = "comment"
const val reviewCreatedAtColumn = "created_at"
const val reviewUpdatedAtColumn = "updated_at"

@Component
class ReviewRepository constructor(private val dataSource: DataSource): IReviewRepository {
    private val logger = LoggerFactory.getLogger(ReviewRepository::class.java)
    private final val mapper: ObjectMapper

    init {
        val javaTimeModule = JavaTimeModule()
        javaTimeModule.addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
        this.mapper = jacksonObjectMapper().registerModule(javaTimeModule).findAndRegisterModules()
    }

    override fun getAllReviews(): List<Review> {
        logger.info("[ReviewRepository] Fetching all reviews...")
        val query: String =
                """SELECT
                    | $reviewIdColumn,
                    | $reviewRestaurantIdColumn,
                    | $reviewReviewerIdColumn, 
                    | $reviewRatingColumn, 
                    | $reviewCommentColumn, 
                    | $reviewCreatedAtColumn,
                    | $reviewUpdatedAtColumn
                    | FROM $reviewsTable
                """.trimMargin()

        return JdbcTemplate(dataSource).query(query) { rs: ResultSet, _: Int ->
            resultSetToReview(rs)
        }
    }

    override fun getAllReviewsForRestaurant(restaurantId: String): List<Review> {
        logger.info("[ReviewRepository] Fetching all reviews for restaurant...")
        val query: String =
                """SELECT
                    | $reviewIdColumn,
                    | $reviewRestaurantIdColumn,
                    | $reviewReviewerIdColumn, 
                    | $reviewRatingColumn, 
                    | $reviewCommentColumn, 
                    | $reviewCreatedAtColumn,
                    | $reviewUpdatedAtColumn
                    | FROM $reviewsTable
                    | WHERE $reviewRestaurantIdColumn = ?
                """.trimMargin()

        return JdbcTemplate(dataSource).query(query, arrayOf(restaurantId)) { rs: ResultSet, _: Int ->
            resultSetToReview(rs)
        }
    }

    override fun getAllReviewsByUser(userId: String): List<Review> {
        logger.info("[ReviewRepository] Fetching all reviews for user...")
        val query: String =
                """SELECT
                    | $reviewIdColumn,
                    | $reviewRestaurantIdColumn,
                    | $reviewReviewerIdColumn, 
                    | $reviewRatingColumn, 
                    | $reviewCommentColumn, 
                    | $reviewCreatedAtColumn,
                    | $reviewUpdatedAtColumn
                    | FROM $reviewsTable
                    | WHERE $reviewReviewerIdColumn = ?
                """.trimMargin()

        return JdbcTemplate(dataSource).query(query, arrayOf(userId)) { rs: ResultSet, _: Int ->
            resultSetToReview(rs)
        }
    }

    override fun createReview(review: Review): Review {
        logger.info("[ReviewRepository] Creating review...")
        val insert = SimpleJdbcInsert(this.dataSource)
                .withTableName(reviewsTable)
                .usingColumns(
                        reviewIdColumn,
                        reviewRestaurantIdColumn,
                        reviewReviewerIdColumn,
                        reviewRatingColumn,
                        reviewCommentColumn,
                        reviewCreatedAtColumn,
                        reviewUpdatedAtColumn
                )
        val parameters = HashMap<String, Any>(1)
        parameters[reviewIdColumn] = review.id
        parameters[reviewRestaurantIdColumn] = review.restaurantId
        parameters[reviewReviewerIdColumn] = review.reviewerId
        parameters[reviewRatingColumn] = review.rating
        parameters[reviewCommentColumn] = review.comment
        parameters[reviewCreatedAtColumn] = review.createdAt
        parameters[reviewUpdatedAtColumn] = review.updatedAt

        insert.execute(parameters)

        return review
    }

    override fun getReview(id: String): Review? {
        logger.info("[ReviewRepository] Fetching review...")
        val query: String =
                """SELECT
                    | $reviewIdColumn,
                    | $reviewRestaurantIdColumn,
                    | $reviewReviewerIdColumn, 
                    | $reviewRatingColumn, 
                    | $reviewCommentColumn, 
                    | $reviewCreatedAtColumn,
                    | $reviewUpdatedAtColumn
                    | FROM $reviewsTable
                    | WHERE $reviewIdColumn = ?
                """.trimMargin()

        val reviewsList = JdbcTemplate(dataSource).query(query, arrayOf(id)) { rs: ResultSet, _: Int ->
            resultSetToReview(rs)
        }

        return if (reviewsList.isEmpty()) {
            null
        } else {
            reviewsList.first()
        }
    }

    override fun updateReview(id: String, review: Review): Boolean {
        logger.info("[ReviewRepository] Updating review...")
        val query: String =
                """UPDATE $reviewsTable
                    | SET $reviewRestaurantIdColumn = ?,
                    | $reviewReviewerIdColumn = ?,
                    | $reviewRatingColumn = ?,
                    | $reviewCommentColumn = ?,
                    | $reviewUpdatedAtColumn = ?
                    | WHERE $reviewIdColumn = ?
                """.trimMargin()

        JdbcTemplate(this.dataSource)
                .update(
                        query,
                        ArgumentPreparedStatementSetter(
                                arrayOf(
                                        review.restaurantId,
                                        review.reviewerId,
                                        review.rating,
                                        review.comment,
                                        review.updatedAt,
                                        id
                                )
                        )
                )

        return true
    }

    override fun deleteReview(id: String): Boolean {
        logger.info("[ReviewRepository] Deleting review...")
        val query: String =
                """DELETE FROM $reviewsTable
                    | WHERE $reviewIdColumn = ?
                """.trimMargin()

        JdbcTemplate(this.dataSource).update(query, ArgumentPreparedStatementSetter(arrayOf(id)))
        return true
    }

    private fun resultSetToReview(resultSet: ResultSet): Review {
        return Review(
                id = resultSet.getString(reviewIdColumn),
                restaurantId = resultSet.getString(reviewRestaurantIdColumn),
                reviewerId = resultSet.getString(reviewReviewerIdColumn),
                rating = resultSet.getInt(reviewRatingColumn),
                comment = resultSet.getString(reviewCommentColumn),
                createdAt = resultSet.getTimestamp(reviewCreatedAtColumn).toLocalDateTime(),
                updatedAt = resultSet.getTimestamp(reviewUpdatedAtColumn).toLocalDateTime(),
        )
    }
}
