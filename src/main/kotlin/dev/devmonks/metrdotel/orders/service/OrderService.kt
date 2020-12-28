package dev.devmonks.metrdotel.orders.service

import dev.devmonks.metrdotel.error.exception.EntityNotFoundException
import dev.devmonks.metrdotel.orders.dto.OrderItemRequest
import dev.devmonks.metrdotel.orders.dto.OrderRequest
import dev.devmonks.metrdotel.orders.model.Order
import dev.devmonks.metrdotel.orders.model.OrderItem
import dev.devmonks.metrdotel.orders.repository.IOrderRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class OrderService @Autowired constructor(private val orderRepository: IOrderRepository): IOrderService {

    private val logger = LoggerFactory.getLogger(OrderService::class.java)

    override fun getAllOrders(): List<Order> {
        logger.info("[OrderService] Fetching all orders...")
        return this.orderRepository.getAllOrders()
    }

    override fun getAllOrdersForRestaurant(restaurantId: String): List<Order> {
        logger.info("[OrderService] Fetching orders for restaurant...")
        return this.orderRepository.getAllOrdersForRestaurant(restaurantId)
    }

    override fun getAllOrdersForUser(userId: String): List<Order> {
        logger.info("[OrderService] Fetching orders for user...")
        return this.orderRepository.getAllOrdersForUser(userId)
    }

    override fun createOrder(orderRequest: OrderRequest, restaurantId: String, userId: String): Order {
        logger.info("[OrderService] Creating order...")
        orderRequest.id = UUID.randomUUID().toString()
        // TODO: Check if restaurant and user exist as well as menu item for each order item
        orderRequest.restaurantId = restaurantId
        orderRequest.userId = userId
        orderRequest.items = orderRequest.items.map{
            OrderItemRequest(
                    id = UUID.randomUUID().toString(),
                    menuItemId = it.menuItemId,
                    orderId = orderRequest.id,
                    quantity = it.quantity
            )
        }

        return this.orderRepository.createOrder(orderRequest.toOrder())
    }

    override fun getOrder(id: String): Order {
        logger.info("[OrderService] Fetching orders...")
        val order = this.orderRepository.getOrder(id)
        if (order !== null) {
            return order
        }
        throw EntityNotFoundException(Order::class.java, "id", id)
    }

    override fun updateOrder(id: String, userId: String, orderRequest: OrderRequest): Boolean {
        logger.info("[OrderService] Updating order...")
        val order = this.getOrder(id)
        orderRequest.id = id
        orderRequest.userId = userId
        orderRequest.items.map {
            if (it.orderId.isNullOrBlank()){
                it.orderId = id
            }
            if (it.id.isNullOrBlank()){
                it.id = UUID.randomUUID().toString()
            }
        }
        val newOrder = orderRequest.toOrder()
        return this.orderRepository.updateOrder(
                id,
                order.copy(
                        items = newOrder.items,
                        discount = newOrder.discount,
                        waiterTip = newOrder.waiterTip,
                        dateTime = newOrder.dateTime,
                        updatedAt = newOrder.updatedAt
                )
        )
    }

    override fun deleteOrder(id: String): Boolean {
        logger.info("[OrderService] Deleting order...")
        this.getOrder(id)
        return this.orderRepository.deleteOrder(id)
    }

    override fun getAllOrderItems(): List<OrderItem> {
        logger.info("[OrderService] Fetching all order items...")
        return this.orderRepository.getAllOrderItems()
    }

    override fun getAllOrderItemsForOrder(orderId: String): List<OrderItem> {
        logger.info("[OrderService] Fetching order items for order...")
        return this.orderRepository.getAllOrderItemsForOrder(orderId)
    }

    override fun createOrderItem(orderItemRequest: OrderItemRequest, orderId: String): OrderItem {
        logger.info("[OrderService] Creating order item...")
        orderItemRequest.id = UUID.randomUUID().toString()
        // TODO: Check if order and menu item exist
        orderItemRequest.orderId = orderId
        return this.orderRepository.createOrderItem(orderItemRequest.toOrderItem())
    }

    override fun getOrderItem(id: String): OrderItem {
        logger.info("[OrderService] Fetching order item...")
        val orderItem = this.orderRepository.getOrderItem(id)
        if (orderItem !== null) {
            return orderItem
        }
        throw EntityNotFoundException(OrderItem::class.java, "id", id)
    }

    override fun updateOrderItem(id: String, orderItemRequest: OrderItemRequest): Boolean {
        logger.info("[OrderService] Updating order item...")
        val orderItem = this.getOrderItem(id)
        val newOrderItem = orderItemRequest.toOrderItem()

        return this.orderRepository.updateOrderItem(
                id,
                orderItem.copy(
                        menuItemId = newOrderItem.menuItemId,
                        quantity = newOrderItem.quantity
                )
        )
    }

    override fun deleteOrderItem(id: String): Boolean {
        logger.info("[OrderService] Deleting order item...")
        this.getOrderItem(id)
        return this.orderRepository.deleteOrderItem(id)
    }
}