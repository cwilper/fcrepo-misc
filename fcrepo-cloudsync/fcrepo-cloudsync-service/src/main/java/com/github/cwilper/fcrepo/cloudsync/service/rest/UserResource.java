package com.github.cwilper.fcrepo.cloudsync.service.rest;

import org.apache.cxf.jaxrs.model.wadl.Description;
import org.apache.cxf.jaxrs.model.wadl.Descriptions;
import org.apache.cxf.jaxrs.model.wadl.DocTarget;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("users")
public class UserResource {

    @POST
    @Path("/")
    @Consumes({"application/xml", "application/json"})
    @Produces({})
    @Descriptions({
        @Description(value = "Creates a user", target = DocTarget.METHOD),
        @Description(value = "Status: 201 Created", target = DocTarget.RESPONSE)
    })
    public Response createUser() {
        Response r = Response.created(null).build();
        return r;
    }

    @GET
    @Path("/")
    @Consumes({})
    @Produces({"application/xml", "application/json"})
    @Descriptions({
        @Description(value = "Lists all users", target = DocTarget.METHOD),
        @Description(value = "Status: 200 OK", target = DocTarget.RESPONSE)
    })
    public Response listUsers() {
        Response r = Response.ok().build();
        return r;
    }

    @GET
    @Path("{id}")
    @Consumes({})
    @Produces({"application/xml", "application/json"})
    @Descriptions({
        @Description(value = "Gets a user", target = DocTarget.METHOD),
        @Description(value = "Status: 200 OK", target = DocTarget.RESPONSE)
    })
    public Response getUser(@PathParam("id") String id) {
        Response r = Response.ok().build();
        return r;
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    @Descriptions({
        @Description(value = "Updates a user", target = DocTarget.METHOD),
        @Description(value = "Status: 200 OK", target = DocTarget.RESPONSE)
    })
    public Response updateUser(@PathParam("id") String id) {
        Response r = Response.ok().build();
        return r;
    }

    @DELETE
    @Path("{id}")
    @Consumes({})
    @Produces({})
    @Descriptions({
        @Description(value = "Deletes a user", target = DocTarget.METHOD),
        @Description(value = "Status: 204 No Content", target = DocTarget.RESPONSE)
    })
    public Response deleteUser(@PathParam("id") String id) {
        Response r = Response.noContent().build();
        return r;
    }

}
