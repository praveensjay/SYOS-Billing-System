package com.syos.controller;

import com.syos.model.Order;
import com.syos.service.CheckoutService;
import com.syos.service.CheckoutServiceImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/checkout")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CheckoutController {

    private final CheckoutService checkoutService = new CheckoutServiceImpl();

    public CheckoutController() {
        System.out.println("✅ CheckoutController initialized!");
    }

    @POST
    public Response processCheckout(Order orderRequest) {
        System.out.println("📢 Received checkout request: " + orderRequest);

        if (orderRequest == null || orderRequest.getItems() == null || orderRequest.getItems().isEmpty()) {
            System.err.println("❌ Error: Order request is invalid or empty.");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"success\": false, \"message\": \"Invalid order data.\"}")
                    .build();
        }

        if (orderRequest.getCustomerId() <= 0) {
            System.err.println("❌ Error: Customer ID is missing or invalid.");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"success\": false, \"message\": \"Customer ID is missing or invalid.\"}")
                    .build();
        }

        System.out.println("✅ Processing order for customer ID: " + orderRequest.getCustomerId());

        boolean orderCreated = checkoutService.processOrder(orderRequest);

        if (!orderCreated) {
            System.err.println("❌ Error processing order in `processOrder()`.");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"success\": false, \"message\": \"Failed to process order.\"}")
                    .build();
        }

        System.out.println("✅ Order placed successfully for Customer ID: " + orderRequest.getCustomerId());

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Order placed successfully!");
        return Response.ok(response).build();
    }

}
