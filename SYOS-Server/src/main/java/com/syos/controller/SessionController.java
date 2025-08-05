package com.syos.controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Path("/session")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SessionController {

    @POST
    public Response createSession(@QueryParam("name") String name, @QueryParam("role") String role, @Context HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        session.setAttribute("name", name);
        session.setAttribute("role", role);
        return Response.ok("{\"message\": \"Session created successfully!\"}").build();
    }

    @GET
    public Response getSession(@Context HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("role") == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\": \"Session not found\"}").build();
        }
        String name = (String) session.getAttribute("name");
        String role = (String) session.getAttribute("role");
        return Response.ok("{\"name\": \"" + name + "\", \"role\": \"" + role + "\"}").build();
    }

    @POST
    @Path("/logout")
    public Response logout(@Context HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return Response.ok("{\"message\": \"Logged out successfully\"}").build();
    }
}
