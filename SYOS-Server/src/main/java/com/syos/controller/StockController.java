package com.syos.controller;

import com.syos.model.Stock;
import com.syos.service.StockManager;
import com.syos.service.StockManagerImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/stock")
public class StockController {

    private final StockManager stockManager;

    public StockController() {
        this.stockManager = new StockManagerImpl();
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStock(Stock stock) {
        try {
            stockManager.addStock(stock);
            return Response.ok("{\"message\": \"✅ Stock added successfully!\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"❌ Failed to add stock: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    @GET
    @Path("/{batchCode}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStockByBatch(@PathParam("batchCode") String batchCode) {
        try {
            Stock stock = stockManager.findByBatchCode(batchCode);
            if (stock != null) {
                return Response.ok(stock).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"❌ Stock not found for batch code: " + batchCode + "\"}")
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"❌ Error fetching stock: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllStock() {
        try {
            List<Stock> stockList = stockManager.findAllStock();
            return Response.ok(stockList).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"❌ Error fetching stock list: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStock(Stock stock) {
        try {
            stockManager.updateStock(stock);
            return Response.ok("{\"message\": \"✅ Stock updated successfully!\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"❌ Failed to update stock: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    @DELETE
    @Path("/delete/{batchCode}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteStock(@PathParam("batchCode") String batchCode) {
        try {
            stockManager.deleteStock(batchCode);
            return Response.ok("{\"message\": \"✅ Stock deleted successfully!\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"❌ Failed to delete stock: " + e.getMessage() + "\"}")
                    .build();
        }
    }
}
