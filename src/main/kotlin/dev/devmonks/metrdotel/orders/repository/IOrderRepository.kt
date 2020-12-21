package dev.devmonks.metrdotel.orders.repository

import dev.devmonks.metrdotel.orders.model.Order
import dev.devmonks.metrdotel.orders.model.OrderItem

interface IOrderRepository {
    fun getAllOrders(): List<Order>
    fun getAllOrdersForRestaurant(restaurantId: String): List<Order>
    fun getAllOrdersForUser(userId: String): List<Order>
    fun createOrder(order: Order): Order
    fun getOrder(id: String): Order?
    fun updateOrder(id: String, order: Order): Boolean
    fun deleteOrder(id: String): Boolean

    fun getAllOrderItems(): List<OrderItem>
    fun getAllOrderItemsForOrder(orderId: String): List<OrderItem>
    fun createOrderItem(orderItem: OrderItem): OrderItem
    fun getOrderItem(id: String): OrderItem?
    fun updateOrderItem(id: String, orderItem: OrderItem): Boolean
    fun deleteOrderItem(id: String): Boolean
}