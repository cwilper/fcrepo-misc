package com.github.cwilper.fcrepo.cloudsync.service.rest;

import org.apache.cxf.jaxrs.model.wadl.Description;
import org.apache.cxf.jaxrs.model.wadl.Descriptions;
import org.apache.cxf.jaxrs.model.wadl.DocTarget;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("logs/system")
public class SystemLogResource {

    @GET
    @Path("/")
    @Consumes({})
    @Produces({"application/xml", "application/json"})
    @Descriptions({
        @Description(value = "Lists all system logs", target = DocTarget.METHOD),
        @Description(value = "Status: 200 OK", target = DocTarget.RESPONSE)
    })
    public Response listSystemLogs() {
        Response r = Response.ok().build();
        return r;
    }

    @GET
    @Path("{id}")
    @Consumes({})
    @Produces({"text/plain"})
    @Descriptions({
        @Description(value = "Gets a system log", target = DocTarget.METHOD),
        @Description(value = "Status: 200 OK", target = DocTarget.RESPONSE)
    })
    public Response getSystemLog(@PathParam("id") String id) {
        Response r = Response.ok().build();
        return r;
    }

    @DELETE
    @Path("{id}")
    @Consumes({})
    @Produces({})
    @Descriptions({
        @Description(value = "Deletes a system log", target = DocTarget.METHOD),
        @Description(value = "Status: 204 No Content", target = DocTarget.RESPONSE)
    })
    public Response deleteSystemLog(@PathParam("id") String id) {
        Response r = Response.noContent().build();
        return r;
    }

}
