package com.syos.controller;

import com.syos.model.User;
import com.syos.service.AuthService;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {
    private final AuthService authService = new AuthService();

    @POST
    @Path("/register")
    public Response registerUser(User user) {
        JsonObject result = authService.registerUser(user);
        return result.containsKey("✅") ? Response.status(Response.Status.CREATED).entity(result).build()
                : Response.status(Response.Status.BAD_REQUEST).entity(result).build();
    }

    @POST
    @Path("/login")
    public Response loginUser(JsonObject credentials) {
        String email = credentials.getString("email", null);
        String password = credentials.getString("password", null);

        if (email == null || password == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"❌ Email and password are required!\"}").build();
        }

        JsonObject response = authService.login(email, password);

        if (response.containsKey("error")) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(response).build();
        }

        // Send the redirect URL in the response JSON
        return Response.ok(response).build();
    }


    @GET
    @Path("/validate")
    public Response validateToken(@QueryParam("token") String token) {
        boolean isValid = authService.validateToken(token);
        return isValid ? Response.ok("✅ Token is valid").build() : Response.status(Response.Status.UNAUTHORIZED).entity("❌ Invalid Token").build();
    }
}
