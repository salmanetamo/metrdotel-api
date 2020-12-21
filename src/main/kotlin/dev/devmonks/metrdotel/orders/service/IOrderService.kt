package dev.devmonks.metrdotel.orders.service

import dev.devmonks.metrdotel.orders.dto.OrderItemRequest
import dev.devmonks.metrdotel.orders.dto.OrderRequest
import dev.devmonks.metrdotel.orders.model.Order
import dev.devmonks.metrdotel.orders.model.OrderItem

interface IOrderService {
    fun getAllOrders(): List<Order>
    fun getAllOrdersForRestaurant(restaurantId: String): List<Order>
    fun getAllOrdersForUser(userId: String): List<Order>
    fun createOrder(orderRequest: OrderRequest, restaurantId: String, userId: String): Order
    fun getOrder(id: String): Order
    fun updateOrder(id: String, userId: String, orderRequest: OrderRequest): Boolean
    fun deleteOrder(id: String): Boolean

    fun getAllOrderItems(): List<OrderItem>
    fun getAllOrderItemsForOrder(orderId: String): List<OrderItem>
    fun createOrderItem(orderItemRequest: OrderItemRequest, orderId: String): OrderItem
    fun getOrderItem(id: String): OrderItem
    fun updateOrderItem(id: String, orderItemRequest: OrderItemRequest): Boolean
    fun deleteOrderItem(id: String): Boolean
}