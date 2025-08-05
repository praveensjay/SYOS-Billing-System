package com.syos.controller;

import com.syos.model.Item;
import com.syos.service.ItemManager;
import com.syos.service.ItemManagerImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemController {

    private final ItemManager itemManager = new ItemManagerImpl();

    public ItemController() {
        System.out.println("✅ ItemController initialized!");
    }

    /**
     * Fetch all items.
     */
    @GET
    public Response getAllItems() {
        List<Item> items = itemManager.getAllItems();
        if (items == null || items.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("No items found.").build();
        }
        return Response.ok(items).build();
    }

    /**
     * Fetch an item by ID.
     */
    @GET
    @Path("/{id}")
    public Response getItemById(@PathParam("id") int id) {
        Item item = itemManager.findById(id);
        if (item == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Item not found with ID: " + id).build();
        }
        return Response.ok(item).build();
    }

    /**
     * Fetch an item by item code.
     */
    @GET
    @Path("/code/{itemCode}")
    public Response getItemByCode(@PathParam("itemCode") String itemCode) {
        Item item = itemManager.findByCode(itemCode);
        if (item == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Item not found with code: " + itemCode).build();
        }
        return Response.ok(item).build();
    }

    /**
     * Add a new item.
     */
    @POST
    public Response addItem(Item item) {
        if (item == null || item.getItemCode() == null || item.getItemName() == null || item.getItemPrice() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid item data.").build();
        }

        itemManager.addItem(item);
        return Response.status(Response.Status.CREATED).entity("Item added successfully!").build();
    }

    /**
     * Update an existing item.
     */
    @PUT
    @Path("/{id}")
    public Response updateItem(@PathParam("id") int id, Item updatedItem) {
        Item existingItem = itemManager.findById(id);
        if (existingItem == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Item not found with ID: " + id).build();
        }

        updatedItem.setItemId(id); // Ensure correct ID is used
        itemManager.updateItem(updatedItem);
        return Response.ok("Item updated successfully!").build();
    }

    /**
     * Delete an item by ID.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteItem(@PathParam("id") int id) {
        Item existingItem = itemManager.findById(id);
        if (existingItem == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Item not found with ID: " + id).build();
        }

        itemManager.removeItem(existingItem.getItemCode());
        return Response.ok("Item deleted successfully!").build();
    }
}
