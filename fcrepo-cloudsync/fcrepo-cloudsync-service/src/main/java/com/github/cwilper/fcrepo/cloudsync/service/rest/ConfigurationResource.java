package com.github.cwilper.fcrepo.cloudsync.service.rest;

import com.github.cwilper.fcrepo.cloudsync.api.Configuration;
import org.apache.cxf.jaxrs.model.wadl.Description;
import org.apache.cxf.jaxrs.model.wadl.Descriptions;
import org.apache.cxf.jaxrs.model.wadl.DocTarget;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("configuration")
public class ConfigurationResource {

    @GET
    @Path("/")
    @Produces({"application/xml", "application/json"})
    @Descriptions({
        @Description(value = "Gets the configuration", target = DocTarget.METHOD),
        @Description(value = "Status: 200 OK", target = DocTarget.RESPONSE)
    })
    public Configuration getConfiguration() {
        return new Configuration();
    }

    @PUT
    @Path("/")
    @Consumes({"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    @Descriptions({
        @Description(value = "Updates the configuration", target = DocTarget.METHOD),
        @Description(value = "Status: 200 OK", target = DocTarget.RESPONSE)
    })
    public Response updateConfiguration() {
        Response r = Response.ok().build();
        return r;
    }

}
