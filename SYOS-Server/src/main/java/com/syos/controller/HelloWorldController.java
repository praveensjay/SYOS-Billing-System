package com.syos.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.syos.dto.MessageDTO;

@Path("/hello")
public class HelloWorldController {

    public HelloWorldController() {
        System.out.println("✅ HelloWorldController initialized!");
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public MessageDTO sayHello() {
        System.out.println("✅ Received request at /api/hello");
        return new MessageDTO("Hello from SYOS-Server!");
    }
}
