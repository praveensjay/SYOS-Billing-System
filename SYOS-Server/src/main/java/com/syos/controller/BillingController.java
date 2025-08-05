package com.syos.controller;

import com.syos.model.Bill;
import com.syos.model.Customer;
import com.syos.model.Item;
import com.syos.model.Transaction;
import com.syos.service.BillingManager;
import com.syos.service.BillingManagerImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/billing")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BillingController {

    private final BillingManager billingManager = new BillingManagerImpl();

    public BillingController() {
        System.out.println("✅ BillingController initialized!");
    }

    /**
     * 📝 Create a new bill for a customer.
     * @param customer The customer object.
     * @return Response with created bill.
     */
    @POST
    @Path("/create")
    public Response createBill(Customer customer) {
        Bill bill = billingManager.createBill(customer);
        if (bill == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("❌ Failed to create bill").build();
        }
        return Response.status(Response.Status.CREATED).entity(bill).build();
    }

    /**
     * 🛒 Add an item to an existing bill.
     * @param billId ID of the bill.
     * @param item Item details.
     * @param quantity Number of items.
     * @return Updated bill.
     */
    @POST
    @Path("/addItem/{billId}")
    public Response addItemToBill(@PathParam("billId") int billId, Item item, @QueryParam("quantity") int quantity) {
        Bill bill = billingManager.getBillById(billId);
        if (bill == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("❌ Bill not found").build();
        }
        billingManager.addItemToBill(bill, item, quantity);
        return Response.ok(bill).build();
    }

    /**
     * 💰 Apply a discount to a bill.
     * @param billId ID of the bill.
     * @param discountRate Discount percentage.
     * @return Updated bill.
     */
    @POST
    @Path("/applyDiscount/{billId}")
    public Response applyDiscount(@PathParam("billId") int billId, @QueryParam("discountRate") double discountRate) {
        Bill bill = billingManager.getBillById(billId);
        if (bill == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("❌ Bill not found").build();
        }
        billingManager.applyDiscount(bill, discountRate);
        return Response.ok(bill).build();
    }

    /**
     * ✅ Finalize a bill (apply tax, loyalty points, and process payment).
     * @param billId ID of the bill.
     * @param cashTendered Amount paid.
     * @param useLoyaltyPoints Whether to use loyalty points.
     * @return Finalized bill.
     */
    @POST
    @Path("/finalize/{billId}")
    public Response finalizeBill(@PathParam("billId") int billId, @QueryParam("cash") double cashTendered, @QueryParam("useLoyalty") boolean useLoyaltyPoints) {
        Bill bill = billingManager.getBillById(billId);
        if (bill == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("❌ Bill not found").build();
        }
        billingManager.finalizeBill(bill, cashTendered, useLoyaltyPoints);
        return Response.ok(bill).build();
    }

    /**
     * 📜 Fetch transactions related to a bill.
     * @param billId ID of the bill.
     * @return List of transactions.
     */
    @GET
    @Path("/transactions/{billId}")
    public Response getTransactionsByBillId(@PathParam("billId") int billId) {
        List<Transaction> transactions = billingManager.getTransactionsByBillId(billId);
        if (transactions == null || transactions.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("❌ No transactions found").build();
        }
        return Response.ok(transactions).build();
    }
}
