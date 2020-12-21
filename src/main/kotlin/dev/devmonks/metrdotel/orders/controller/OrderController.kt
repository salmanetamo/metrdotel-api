package dev.devmonks.metrdotel.orders.controller

import dev.devmonks.metrdotel.orders.dto.OrderItemRequest
import dev.devmonks.metrdotel.orders.dto.OrderRequest
import dev.devmonks.metrdotel.orders.model.Order
import dev.devmonks.metrdotel.orders.model.OrderItem
import dev.devmonks.metrdotel.orders.service.IOrderService
import dev.devmonks.metrdotel.restaurants.dto.MenuItemRequest
import dev.devmonks.metrdotel.restaurants.model.MenuItem
import dev.devmonks.metrdotel.users.service.IUserService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["*"])
@RestController
@RequestMapping("/orders")
@Api("Operations related to orders")
class OrderController @Autowired constructor(
        private val orderService: IOrderService,
        private val userService: IUserService
        ) {

    private val logger = LoggerFactory.getLogger(OrderController::class.java)

    @Throws(Exception::class)
    @ApiOperation("Create a new order")
    @ApiResponses(value = [
        ApiResponse(code = 201, message = "Returns new restaurant", response = Order::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @PostMapping("/{restaurantId}")
    fun createOrder(@RequestBody payload: OrderRequest, @PathVariable restaurantId: String): ResponseEntity<*> {
        logger.info("Create new order request...")
        return ResponseEntity(
                this.orderService.createOrder(
                        payload,
                        restaurantId,
                        this.userService.getUserByEmail(
                                SecurityContextHolder.getContext().authentication.principal.toString()
                        ).id
                ),
                HttpStatus.CREATED
        )
    }

    @Throws(Exception::class)
    @ApiOperation("Gets a list of all orders")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns list of all orders", response = Array<Order>::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @GetMapping("")
    fun getAllOrders(): ResponseEntity<*> {
        logger.info("Get all orders request...")
        return ResponseEntity(this.orderService.getAllOrders(), HttpStatus.OK)
    }

    @Throws(Exception::class)
    @ApiOperation("Gets a list of all orders for restaurant")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns list of all orders for restaurant", response = Array<Order>::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @GetMapping("/restaurant/{restaurantId}")
    fun getAllOrdersForRestaurant(@PathVariable restaurantId: String): ResponseEntity<*> {
        logger.info("Get all orders for restaurant request...")
        return ResponseEntity(this.orderService.getAllOrdersForRestaurant(restaurantId), HttpStatus.OK)
    }

    @Throws(Exception::class)
    @ApiOperation("Get single order")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns order", response = Order::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 404, message = "Order not found"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @GetMapping("/{id}")
    fun getOrder(@PathVariable id: String): ResponseEntity<*> {
        logger.info("Get order request...")
        return ResponseEntity(this.orderService.getOrder(id), HttpStatus.OK)
    }

    @Throws(Exception::class)
    @ApiOperation("Update order")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns whether order was updated", response = Boolean::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 404, message = "Order not found"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @PutMapping("/{id}")
    fun updateOrder(@PathVariable id: String, @RequestBody payload: OrderRequest): ResponseEntity<*> {
        logger.info("Update order request...")
        return ResponseEntity(
                this.orderService.updateOrder(
                        id,
                        this.userService.getUserByEmail(
                                SecurityContextHolder.getContext().authentication.principal.toString()
                        ).id,
                        payload
                ),
                HttpStatus.OK
        )
    }

    @Throws(Exception::class)
    @ApiOperation("Delete order")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns whether order was deleted", response = Boolean::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 404, message = "Order not found"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @DeleteMapping("/{id}")
    fun deleteOrder(@PathVariable id: String): ResponseEntity<*> {
        logger.info("Delete order request...")
        return ResponseEntity(this.orderService.deleteOrder(id), HttpStatus.OK)
    }

    @Throws(Exception::class)
    @ApiOperation("Create a new order item")
    @ApiResponses(value = [
        ApiResponse(code = 201, message = "Returns new order item", response = OrderItem::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @PostMapping("/items/{orderId}")
    fun createOrderItem(@RequestBody payload: OrderItemRequest, @PathVariable orderId: String): ResponseEntity<*> {
        logger.info("Create new order item request...")
        return ResponseEntity(this.orderService.createOrderItem(payload, orderId), HttpStatus.CREATED)
    }

    @Throws(Exception::class)
    @ApiOperation("Gets a list of all order items")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns list of all order items", response = Array<OrderItem>::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @GetMapping("/items")
    fun getAllOrderItems(): ResponseEntity<*> {
        logger.info("Get all order items request...")
        return ResponseEntity(this.orderService.getAllOrderItems(), HttpStatus.OK)
    }

    @Throws(Exception::class)
    @ApiOperation("Gets a list of all order items for order")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns list of all order items for order", response = Array<OrderItem>::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @GetMapping("/{orderId}/items")
    fun getAllOrderItemsForOrder(@PathVariable orderId: String): ResponseEntity<*> {
        logger.info("Get all order items for order request...")
        return ResponseEntity(this.orderService.getAllOrderItemsForOrder(orderId), HttpStatus.OK)
    }

    @Throws(Exception::class)
    @ApiOperation("Get single order item")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns order item", response = OrderItem::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 404, message = "Menu item not found"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @GetMapping("/items/{id}")
    fun getOrderItem(@PathVariable id: String): ResponseEntity<*> {
        logger.info("Get order item request...")
        return ResponseEntity(this.orderService.getOrderItem(id), HttpStatus.OK)
    }

    @Throws(Exception::class)
    @ApiOperation("Update order item")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns whether order item was updated", response = Boolean::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 404, message = "Menu item not found"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @PutMapping("/items/{id}")
    fun updateOrderItem(@PathVariable id: String, @RequestBody payload: OrderItemRequest): ResponseEntity<*> {
        logger.info("Update order item request...")
        return ResponseEntity(this.orderService.updateOrderItem(id, payload), HttpStatus.OK)
    }

    @Throws(Exception::class)
    @ApiOperation("Delete order item")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns whether order item was deleted", response = Boolean::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 404, message = "Menu item not found"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @DeleteMapping("/items/{id}")
    fun deleteOrderItem(@PathVariable id: String): ResponseEntity<*> {
        logger.info("Delete order item request...")
        return ResponseEntity(this.orderService.deleteOrderItem(id), HttpStatus.OK)
    }

}