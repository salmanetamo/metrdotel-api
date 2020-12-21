package dev.devmonks.metrdotel.orders.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import dev.devmonks.metrdotel.orders.model.Order
import dev.devmonks.metrdotel.orders.model.OrderItem
import dev.devmonks.metrdotel.restaurants.repository.*
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Component
import java.sql.ResultSet
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.sql.DataSource

const val ordersTable = "orders"
const val orderIdColumn = "id"
const val orderRestaurantIdColumn = "restaurant_id"
const val orderUserIdColumn = "user_id"
const val orderDiscountColumn = "discount"
const val orderWaiterTipColumn = "waiter_tip"
const val orderDatetimeColumn = "datetime"
const val orderCreatedAtColumn = "created_at"
const val orderUpdatedAtColumn = "updated_at"

const val orderItemsTable = "order_items"
const val orderItemIdColumn = "id"
const val orderItemOrderIdColumn = "order_id"
const val orderItemMenuItemIdColumn = "menu_item_id"
const val orderItemQuantityColumn = "quantity"

@Component
class OrderRepository constructor(private val dataSource: DataSource): IOrderRepository {

    private val logger = LoggerFactory.getLogger(OrderRepository::class.java)
    private final val mapper: ObjectMapper

    init {
        val javaTimeModule = JavaTimeModule()
        javaTimeModule.addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
        this.mapper = jacksonObjectMapper().registerModule(javaTimeModule).findAndRegisterModules()
    }

    override fun getAllOrders(): List<Order> {
        logger.info("[OrderRepository] Fetching all orders...")
        val query: String =
                """SELECT
                    | $orderIdColumn,
                    | $orderRestaurantIdColumn,
                    | $orderUserIdColumn, 
                    | $orderDiscountColumn, 
                    | $orderWaiterTipColumn, 
                    | $orderDatetimeColumn, 
                    | $orderCreatedAtColumn,
                    | $orderUpdatedAtColumn
                    | FROM $ordersTable
                """.trimMargin()

        return JdbcTemplate(dataSource).query(query){
            rs: ResultSet, _: Int -> resultSetToOrder(rs)
        }
    }

    override fun getAllOrdersForRestaurant(restaurantId: String): List<Order> {
        logger.info("[OrderRepository] Fetching all orders for restaurant...")
        val query: String =
                """SELECT
                    | $orderIdColumn,
                    | $orderRestaurantIdColumn,
                    | $orderUserIdColumn, 
                    | $orderDiscountColumn, 
                    | $orderWaiterTipColumn, 
                    | $orderDatetimeColumn, 
                    | $orderCreatedAtColumn,
                    | $orderUpdatedAtColumn
                    | FROM $ordersTable
                    | WHERE $orderRestaurantIdColumn = ?
                """.trimMargin()

        return JdbcTemplate(dataSource).query(query, arrayOf(restaurantId)){
            rs: ResultSet, _: Int -> resultSetToOrder(rs)
        }
    }

    override fun getAllOrdersForUser(userId: String): List<Order> {
        logger.info("[OrderRepository] Fetching all orders for user...")
        val query: String =
                """SELECT
                    | $orderIdColumn,
                    | $orderRestaurantIdColumn,
                    | $orderUserIdColumn, 
                    | $orderDiscountColumn, 
                    | $orderWaiterTipColumn, 
                    | $orderDatetimeColumn, 
                    | $orderCreatedAtColumn,
                    | $orderUpdatedAtColumn
                    | FROM $ordersTable
                    | WHERE $orderUserIdColumn = ?
                """.trimMargin()

        return JdbcTemplate(dataSource).query(query, arrayOf(userId)){
            rs: ResultSet, _: Int -> resultSetToOrder(rs)
        }
    }

    override fun createOrder(order: Order): Order {
        logger.info("[OrderRepository] Creating order...")
        val insert = SimpleJdbcInsert(this.dataSource)
                .withTableName(ordersTable)
                .usingColumns(
                        orderIdColumn,
                        orderRestaurantIdColumn,
                        orderUserIdColumn,
                        orderDiscountColumn,
                        orderWaiterTipColumn,
                        orderDatetimeColumn,
                        orderCreatedAtColumn,
                        orderUpdatedAtColumn
                )
        val parameters = HashMap<String, Any>(1)
        parameters[orderIdColumn] = order.id
        parameters[orderRestaurantIdColumn] = order.restaurantId
        parameters[orderUserIdColumn] = order.userId
        parameters[orderDiscountColumn] = order.discount
        parameters[orderWaiterTipColumn] = order.waiterTip
        parameters[orderDatetimeColumn] = order.dateTime
        parameters[orderCreatedAtColumn] = order.createdAt
        parameters[orderUpdatedAtColumn] = order.updatedAt

        insert.execute(parameters)
        order.items.map { createOrderItem(it) }

        return order
    }

    override fun getOrder(id: String): Order? {
        logger.info("[OrderRepository] Fetching order...")
        val query: String =
                """SELECT
                    | $orderIdColumn,
                    | $orderRestaurantIdColumn,
                    | $orderUserIdColumn, 
                    | $orderDiscountColumn, 
                    | $orderWaiterTipColumn, 
                    | $orderDatetimeColumn, 
                    | $orderCreatedAtColumn,
                    | $orderUpdatedAtColumn
                    | FROM $ordersTable
                    | WHERE $orderIdColumn = ?
                """.trimMargin()

        val ordersList = JdbcTemplate(dataSource).query(query, arrayOf(id)){
            rs: ResultSet, _: Int -> resultSetToOrder(rs)
        }

        return if (ordersList.isEmpty()) {
            null
        } else {
            ordersList.first()
        }
    }

    override fun updateOrder(id: String, order: Order): Boolean {
        logger.info("[OrderRepository] Updating order...")
        val query: String =
                """UPDATE $ordersTable
                    | SET $orderRestaurantIdColumn = ?,
                    | $orderUserIdColumn = ?,
                    | $orderDiscountColumn = ?,
                    | $orderWaiterTipColumn = ?,
                    | $orderDatetimeColumn = ?,
                    | $orderUpdatedAtColumn = ?
                    | WHERE $orderIdColumn = ?
                """.trimMargin()

        JdbcTemplate(this.dataSource)
                .update(
                        query,
                        ArgumentPreparedStatementSetter(
                                arrayOf(
                                        order.restaurantId,
                                        order.userId,
                                        order.discount,
                                        order.waiterTip,
                                        order.dateTime,
                                        order.updatedAt,
                                        id
                                )
                        )
                )

        order.items.map {
            if (this.getOrderItem(it.id) !== null) {
                this.updateOrderItem(it.id, it)
            } else {
                this.createOrderItem(it)
            }
        }

        return true
    }

    override fun deleteOrder(id: String): Boolean {
        logger.info("[OrderRepository] Deleting order...")
        val query: String =
                """DELETE FROM $ordersTable
                    | WHERE $orderIdColumn = ?
                """.trimMargin()

        JdbcTemplate(this.dataSource).update(query, ArgumentPreparedStatementSetter(arrayOf(id)))
        return true
    }

    override fun getAllOrderItems(): List<OrderItem> {
        logger.info("[OrderRepository] Fetching all order items...")
        val query: String =
                """SELECT
                    | $orderItemIdColumn,
                    | $orderItemOrderIdColumn,
                    | $orderItemMenuItemIdColumn,
                    | $orderItemQuantityColumn
                    | FROM $orderItemsTable
                """.trimMargin()

        return JdbcTemplate(dataSource).query(query){
            rs: ResultSet, _: Int -> resultSetToOrderItem(rs)
        }
    }

    override fun getAllOrderItemsForOrder(orderId: String): List<OrderItem> {
        logger.info("[OrderRepository] Fetching all order items for order...")
        val query: String =
                """SELECT
                    | $orderItemIdColumn,
                    | $orderItemOrderIdColumn,
                    | $orderItemMenuItemIdColumn,
                    | $orderItemQuantityColumn
                    | FROM $orderItemsTable
                    | WHERE $orderItemOrderIdColumn = ?
                """.trimMargin()

        return JdbcTemplate(dataSource).query(query, arrayOf(orderId)){
            rs: ResultSet, _: Int -> resultSetToOrderItem(rs)
        }
    }

    override fun createOrderItem(orderItem: OrderItem): OrderItem {
        logger.info("[OrderRepository] Creating order item...")
        val insert = SimpleJdbcInsert(this.dataSource)
                .withTableName(orderItemsTable)
                .usingColumns(
                        orderItemIdColumn,
                        orderItemOrderIdColumn,
                        orderItemMenuItemIdColumn,
                        orderItemQuantityColumn
                )

        val parameters = HashMap<String, Any>(1)

        parameters[orderItemIdColumn] = orderItem.id
        parameters[orderItemOrderIdColumn] = orderItem.orderId
        parameters[orderItemMenuItemIdColumn] = orderItem.menuItemId
        parameters[orderItemQuantityColumn] = orderItem.quantity

        insert.execute(parameters)
        return orderItem
    }

    override fun getOrderItem(id: String): OrderItem? {
        logger.info("[OrderRepository] Fetching order item...")
        val query: String =
                """SELECT
                    | $orderItemIdColumn,
                    | $orderItemOrderIdColumn,
                    | $orderItemMenuItemIdColumn,
                    | $orderItemQuantityColumn
                    | FROM $orderItemsTable
                    | WHERE $orderItemIdColumn = ?
                """.trimMargin()

        val orderItemsList = JdbcTemplate(dataSource).query(query, arrayOf(id)){
            rs: ResultSet, _: Int -> resultSetToOrderItem(rs)
        }

        return if (orderItemsList.isEmpty()) {
            null
        } else {
            orderItemsList.first()
        }
    }

    override fun updateOrderItem(id: String, orderItem: OrderItem): Boolean {
        logger.info("[OrderRepository] Updating order item...")
        val query: String =
                """UPDATE $orderItemsTable
                    |  SET $orderItemOrderIdColumn = ?,
                    | $orderItemMenuItemIdColumn = ?,
                    | $orderItemQuantityColumn = ?
                    | WHERE $orderItemIdColumn = ?
                """.trimMargin()

        JdbcTemplate(this.dataSource)
                .update(
                        query,
                        ArgumentPreparedStatementSetter(
                                arrayOf(
                                        orderItem.orderId,
                                        orderItem.menuItemId,
                                        orderItem.quantity,
                                        id
                                )
                        )
                )

        return true
    }

    override fun deleteOrderItem(id: String): Boolean {
        logger.info("[OrderRepository] Deleting order item...")
        val query: String =
                """DELETE FROM $orderItemsTable
                    | WHERE $orderItemIdColumn = ?
                """.trimMargin()

        JdbcTemplate(this.dataSource).update(query, ArgumentPreparedStatementSetter(arrayOf(id)))
        return true
    }

    private fun resultSetToOrder(resultSet: ResultSet): Order {
        return Order(
                id = resultSet.getString(orderIdColumn),
                restaurantId = resultSet.getString(orderRestaurantIdColumn),
                userId = resultSet.getString(orderUserIdColumn),
                items = this.getAllOrderItemsForOrder(resultSet.getString(orderIdColumn)),
                discount = resultSet.getDouble(orderDiscountColumn),
                waiterTip = resultSet.getDouble(orderWaiterTipColumn),
                dateTime = resultSet.getTimestamp(orderDatetimeColumn).toLocalDateTime(),
                createdAt = resultSet.getTimestamp(orderCreatedAtColumn).toLocalDateTime(),
                updatedAt = resultSet.getTimestamp(orderUpdatedAtColumn).toLocalDateTime(),
        )
    }

    private fun resultSetToOrderItem(resultSet: ResultSet): OrderItem {
        return OrderItem(
                id = resultSet.getString(orderItemIdColumn),
                orderId = resultSet.getString(orderItemOrderIdColumn),
                menuItemId = resultSet.getString(orderItemMenuItemIdColumn),
                quantity = resultSet.getInt(orderItemQuantityColumn)
        )
    }
}