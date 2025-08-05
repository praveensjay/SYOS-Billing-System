package com.syos.controller;

import com.syos.model.Order;
import com.syos.service.OrderService;
import com.syos.service.OrderServiceImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderController {

    private final OrderService orderService = new OrderServiceImpl();

    public OrderController() {
        System.out.println("✅ OrderController initialized!");
    }

    /**
     * Fetch all orders for a given customer ID.
     */
    @GET
    @Path("/customer/{customerId}")
    public Response getOrdersByCustomerId(@PathParam("customerId") int customerId) {
        System.out.println("📢 Fetching orders for customer ID: " + customerId);

        if (customerId <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"success\": false, \"message\": \"Invalid customer ID.\"}")
                    .build();
        }

        List<Order> orders = orderService.getOrdersByCustomerId(customerId);
        if (orders == null || orders.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"success\": false, \"message\": \"No orders found for this customer.\"}")
                    .build();
        }
        return Response.ok(orders).build();
    }

    /**
     * Fetch order details by order ID.
     */
    @GET
    @Path("/{orderId}")
    public Response getOrderById(@PathParam("orderId") int orderId) {
        System.out.println("📢 Fetching order details for order ID: " + orderId);

        if (orderId <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"success\": false, \"message\": \"Invalid order ID.\"}")
                    .build();
        }

        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"success\": false, \"message\": \"Order not found.\"}")
                    .build();
        }

        return Response.ok(order).build();
    }
}
