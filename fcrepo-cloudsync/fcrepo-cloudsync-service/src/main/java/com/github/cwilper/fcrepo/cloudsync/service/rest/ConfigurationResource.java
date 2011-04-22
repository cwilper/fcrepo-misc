package com.github.cwilper.fcrepo.cloudsync.service.rest;

import org.apache.cxf.jaxrs.model.wadl.Description;
import org.apache.cxf.jaxrs.model.wadl.Descriptions;
import org.apache.cxf.jaxrs.model.wadl.DocTarget;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("configuration")
public class ConfigurationResource {

    @GET
    @Path("{id}")
    @Consumes({})
    @Produces({"application/xml", "application/json"})
    @Descriptions({
        @Description(value = "Gets the configuration", target = DocTarget.METHOD),
        @Description(value = "Status: 200 OK", target = DocTarget.RESPONSE)
    })
    public Response getConfiguration(@PathParam("id") String id) {
        Response r = Response.ok().build();
        return r;
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    @Descriptions({
        @Description(value = "Updates the configuration", target = DocTarget.METHOD),
        @Description(value = "Status: 200 OK", target = DocTarget.RESPONSE)
    })
    public Response updateConfiguration(@PathParam("id") String id) {
        Response r = Response.ok().build();
        return r;
    }

}
