package com.github.cwilper.fcrepo.cloudsync.api;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

public class API {
    
    @GET
    @Path("/tasks/")
    public Response listTasks() {
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
