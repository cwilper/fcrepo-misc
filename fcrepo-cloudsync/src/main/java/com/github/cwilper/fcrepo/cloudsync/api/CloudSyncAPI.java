package com.github.cwilper.fcrepo.cloudsync.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

public class CloudSyncAPI {

    private static final Logger logger = LoggerFactory.getLogger(CloudSyncAPI.class);

    @GET
    @Path("/tasks/")
    public Response listTasks() {
        logger.info("Got request to listTasks");
        Response r = Response.ok("Hello").build();
        return r;
    }

/*
    @PUT
    @Path("/customers/{id}")
    Response updateCustomer(@PathParam("id") Long id, Customer customer);
    
    @POST
    @Path("/customers/")
    Response addCustomer(Customer customer);

    @DELETE
    @Path("/customers/{id}/")
    Response deleteCustomer(@PathParam("id") String id);

    @Path("/orders/{orderId}/")
    Order getOrder(@PathParam("orderId") String orderId);
*/
}
