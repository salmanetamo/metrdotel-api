package dev.devmonks.metrdotel.restaurants.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import dev.devmonks.metrdotel.restaurants.model.MenuItem
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

const val menuItemsTable = "menu_items"

const val menuItemIdColumn = "id"
const val menuItemRestaurantIdColumn = "restaurant_id"
const val menuItemNameColumn = "name"
const val menuItemPictureColumn = "picture"
const val menuItemPriceColumn = "price"
const val menuItemDescriptionColumn = "description"
const val menuItemTypesColumn = "types"
const val menuItemCreatedAtColumn = "created_at"
const val menuItemUpdatedAtColumn = "updated_at"

@Component
class MenuRepository @Autowired constructor(private val dataSource: DataSource): IMenuRepository {

    private val logger = LoggerFactory.getLogger(MenuRepository::class.java)

    private final val mapper: ObjectMapper

    init {
        val javaTimeModule = JavaTimeModule()
        javaTimeModule.addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
        this.mapper = jacksonObjectMapper().registerModule(javaTimeModule).findAndRegisterModules()
    }

    override fun getAllMenuItems(): List<MenuItem> {
        logger.info("[MenuRepository] Fetching all menu items for restaurants...")
        val query =
                """SELECT
                    | $menuItemIdColumn,
                    | $menuItemRestaurantIdColumn,
                    | $menuItemNameColumn,
                    | $menuItemPictureColumn,
                    | $menuItemPriceColumn,
                    | $menuItemDescriptionColumn,
                    | $menuItemTypesColumn,
                    | $menuItemCreatedAtColumn,
                    | $menuItemUpdatedAtColumn
                    | FROM $menuItemsTable
                """.trimMargin()

        return JdbcTemplate(dataSource).query(query){
            rs: ResultSet, _: Int -> resultSetToMenuItem(rs)
        }
    }

    override fun getAllMenuItemsForRestaurant(restaurantId: String): List<MenuItem> {
        logger.info("[MenuRepository] Fetching all menu items for restaurants...")
        val query =
                """SELECT
                    | $menuItemIdColumn,
                    | $menuItemRestaurantIdColumn,
                    | $menuItemNameColumn,
                    | $menuItemPictureColumn,
                    | $menuItemPriceColumn,
                    | $menuItemDescriptionColumn,
                    | $menuItemTypesColumn,
                    | $menuItemCreatedAtColumn,
                    | $menuItemUpdatedAtColumn
                    | FROM $menuItemsTable
                    | WHERE $menuItemRestaurantIdColumn = ?
                """.trimMargin()

        return JdbcTemplate(dataSource).query(query, arrayOf(restaurantId)){
            rs: ResultSet, _: Int -> resultSetToMenuItem(rs)
        }
    }

    override fun createMenuItem(menuItem: MenuItem): MenuItem {
        logger.info("[MenuRepository] Creating menu item...")
        val insert = SimpleJdbcInsert(this.dataSource)
                .withTableName(menuItemsTable)
                .usingColumns(
                        menuItemIdColumn,
                        menuItemRestaurantIdColumn,
                        menuItemNameColumn,
                        menuItemPictureColumn,
                        menuItemPriceColumn,
                        menuItemDescriptionColumn,
                        menuItemTypesColumn,
                        menuItemCreatedAtColumn,
                        menuItemUpdatedAtColumn,
                )
        val parameters = HashMap<String, Any>(1)
        parameters[menuItemIdColumn] = menuItem.id
        parameters[menuItemRestaurantIdColumn] = menuItem.restaurantId
        parameters[menuItemNameColumn] = menuItem.name
        parameters[menuItemPictureColumn] = menuItem.picture
        parameters[menuItemPriceColumn] = menuItem.price
        parameters[menuItemDescriptionColumn] = menuItem.description
        parameters[menuItemTypesColumn] = menuItem.types.map{ it }.toTypedArray()
        parameters[menuItemCreatedAtColumn] = menuItem.createdAt
        parameters[menuItemUpdatedAtColumn] = menuItem.updatedAt

        insert.execute(parameters)
        return menuItem
    }

    override fun getMenuItem(id: String): MenuItem? {
        logger.info("[MenuRepository] Fetching menu item...")
        val query =
                """SELECT
                    | $menuItemIdColumn,
                    | $menuItemRestaurantIdColumn,
                    | $menuItemNameColumn,
                    | $menuItemPictureColumn,
                    | $menuItemPriceColumn,
                    | $menuItemDescriptionColumn,
                    | $menuItemTypesColumn,
                    | $menuItemCreatedAtColumn,
                    | $menuItemUpdatedAtColumn
                    | FROM $menuItemsTable
                    | WHERE $menuItemIdColumn = ?
                """.trimMargin()

        val menuItemList = JdbcTemplate(dataSource).query(query, arrayOf(id)){
            rs: ResultSet, _: Int -> resultSetToMenuItem(rs)
        }

        return if (menuItemList.isEmpty()) {
            null
        } else {
            menuItemList.first()
        }
    }

    override fun updateMenuItem(id: String, menuItem: MenuItem): Boolean {
        logger.info("[MenuRepository] Updating menu item...")
        val query: String =
                """UPDATE $menuItemsTable
                    | SET $menuItemRestaurantIdColumn = ?,
                    | $menuItemNameColumn = ?,
                    | $menuItemPictureColumn = ?,
                    | $menuItemPriceColumn = ?,
                    | $menuItemDescriptionColumn = ?,
                    | $menuItemTypesColumn = ?,
                    | $menuItemUpdatedAtColumn = ?
                    | WHERE $menuItemIdColumn = ?
                """.trimMargin()

        JdbcTemplate(this.dataSource)
                .update(
                        query,
                        ArgumentPreparedStatementSetter(
                                arrayOf(
                                        menuItem.restaurantId,
                                        menuItem.name,
                                        menuItem.picture,
                                        menuItem.price,
                                        menuItem.description,
                                        menuItem.types.toTypedArray(),
                                        menuItem.updatedAt,
                                        id
                                )
                        )
                )

        return true
    }

    override fun addPicture(id: String, fileName: String): Boolean {
        val query: String =
        """UPDATE $menuItemsTable
                    | SET $menuItemPictureColumn = ?
                    | WHERE $menuItemIdColumn = ?
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

    override fun deleteMenuItem(id: String): Boolean {
        logger.info("[MenuRepository] Deleting menu item...")
        val query: String =
                """DELETE FROM $menuItemsTable
                    | WHERE $menuItemIdColumn = ?
                """.trimMargin()

        JdbcTemplate(this.dataSource).update(query, ArgumentPreparedStatementSetter(arrayOf(id)))
        return true
    }

    private fun resultSetToMenuItem(resultSet: ResultSet): MenuItem {
        return MenuItem(
                id = resultSet.getString(menuItemIdColumn),
                restaurantId = resultSet.getString(menuItemRestaurantIdColumn),
                name = resultSet.getString(menuItemNameColumn),
                picture = resultSet.getString(menuItemPictureColumn),
                price = resultSet.getDouble(menuItemPriceColumn),
                description = resultSet.getString(menuItemDescriptionColumn),
                types = (resultSet.getArray(menuItemTypesColumn).array as Array<String>).map { it },
                createdAt = resultSet.getTimestamp(menuItemCreatedAtColumn).toLocalDateTime(),
                updatedAt = resultSet.getTimestamp(menuItemUpdatedAtColumn).toLocalDateTime(),
        )
    }
}