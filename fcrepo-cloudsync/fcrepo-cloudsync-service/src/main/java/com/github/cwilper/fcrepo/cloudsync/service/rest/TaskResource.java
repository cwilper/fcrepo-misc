package com.github.cwilper.fcrepo.cloudsync.service.rest;

import com.github.cwilper.fcrepo.cloudsync.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("tasks")
public class TaskResource {

    private static final Logger logger =
            LoggerFactory.getLogger(TaskResource.class);

    @GET
    public Response list() {
        logger.info("Got request to list");
        Response r = Response.ok("Here's the list of tasks").build();
        return r;
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Task show(@PathParam("id") String id) {
        logger.info("Got request to show");
        Task task = new Task();
        task.setId(id);
        return task;
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
