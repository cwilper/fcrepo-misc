package com.github.cwilper.fcrepo.cloudsync.api.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/tasks/")
public class TaskResource {

    private static final Logger logger =
            LoggerFactory.getLogger(TaskResource.class);

    @GET
    @Path("/")
    public Response list() {
        logger.info("Got request to list");
        Response r = Response.ok("Here's the list of tasks").build();
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
