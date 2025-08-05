package com.syos.controller;

import com.syos.model.Customer;
import com.syos.service.CustomerManager;
import com.syos.service.CustomerManagerImpl;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerController {

    private final CustomerManager customerManager = new CustomerManagerImpl();

    public CustomerController() {
        System.out.println("✅ CustomerController initialized!");
    }

    /**
     * Fetch all customers.
     */
    @GET
    public Response getAllCustomers() {
        List<Customer> customers = customerManager.findAllCustomers();
        if (customers == null || customers.isEmpty()) {
            JsonObject errorResponse = Json.createObjectBuilder()
                    .add("error", "No customers found.")
                    .build();
            return Response.status(Response.Status.NOT_FOUND).entity(errorResponse).build();
        }
        return Response.ok(customers).build();
    }

    /**
     * Fetch a customer by ID.
     */
    @GET
    @Path("/{id}")
    public Response getCustomerById(@PathParam("id") int id) {
        Customer customer = customerManager.findCustomerById(id);
        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Customer not found with ID: " + id).build();
        }
        return Response.ok(customer).build();
    }

    /**
     * Fetch a customer by phone number.
     */
    @GET
    @Path("/phone/{phoneNumber}")
    public Response getCustomerByPhone(@PathParam("phoneNumber") String phoneNumber) {
        Customer customer = customerManager.findCustomerByPhoneNumber(phoneNumber);
        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Customer not found with phone number: " + phoneNumber).build();
        }
        return Response.ok(customer).build();
    }

    /**
     * Fetch a customer by email.
     */
    @GET
    @Path("/email/{email}")
    public Response getCustomerByEmail(@PathParam("email") String email) {
        Customer customer = customerManager.findCustomerByEmail(email);
        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Customer not found with email: " + email).build();
        }
        return Response.ok(customer).build();
    }

    /**
     * Add a new customer.
     */
    @POST
    public Response addCustomer(Customer customer) {
        if (customer == null || customer.getName() == null || customer.getPhoneNumber() == null || customer.getEmail() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid customer data.").build();
        }

        customerManager.addCustomer(customer);
        return Response.status(Response.Status.CREATED).entity("Customer added successfully!").build();
    }

    /**
     * Update an existing customer.
     */
    @PUT
    @Path("/{id}")
    public Response updateCustomer(@PathParam("id") int id, Customer updatedCustomer) {
        Customer existingCustomer = customerManager.findCustomerById(id);
        if (existingCustomer == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Customer not found with ID: " + id).build();
        }

        updatedCustomer.setCustomerId(id); // Ensure correct ID is used
        customerManager.updateCustomer(updatedCustomer);
        return Response.ok("Customer updated successfully!").build();
    }

    /**
     * Delete a customer by ID.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") int id) {
        Customer existingCustomer = customerManager.findCustomerById(id);
        if (existingCustomer == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Customer not found with ID: " + id).build();
        }

        customerManager.removeCustomer(id);
        return Response.ok("Customer deleted successfully!").build();
    }
}
