package com.github.cwilper.fcrepo.cloudsync.service.rest;

import com.github.cwilper.fcrepo.cloudsync.api.CloudSyncService;
import com.github.cwilper.fcrepo.cloudsync.api.ObjectSet;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Path("objectsets")
public class ObjectSetResource extends AbstractResource {

    public ObjectSetResource(CloudSyncService service) {
        super(service);
    }

    @POST
    @Path("/")
    @Consumes({XML, JSON})
    @Descriptions({
        @Description(value = "Creates an object set", target = DocTarget.METHOD),
        @Description(value = STATUS_201_CREATED, target = DocTarget.RESPONSE)
    })
    public Response createObjectSet(@Context UriInfo uriInfo,
                                    ObjectSet objectSet) {
        ObjectSet newObjectSet = service.createObjectSet(objectSet);
        URI uri = getResourceURI(uriInfo.getRequestUri(), newObjectSet.id);
        return Response.created(uri).entity(newObjectSet).build();
    }

    @GET
    @Path("/")
    @Produces({XML, JSON})
    @Descriptions({
        @Description(value = "Lists all object sets", target = DocTarget.METHOD),
        @Description(value = STATUS_200_OK, target = DocTarget.RESPONSE)
    })
    public ObjectSets listObjectSets() {
        ObjectSets list = new ObjectSets();
        list.setObjectset(service.listObjectSets());
        return list;
    }

    @GET
    @Path("{id}")
    @Produces({XML, JSON})
    @Descriptions({
        @Description(value = "Gets an object set", target = DocTarget.METHOD),
        @Description(value = STATUS_200_OK, target = DocTarget.RESPONSE)
    })
    public ObjectSet getObjectSet(@PathParam("id") String id) {
        return service.getObjectSet(id);
    }

    @PUT
    @Path("{id}")
    @Consumes({XML, JSON})
    @Produces({XML, JSON})
    @Descriptions({
        @Description(value = "Updates an object set", target = DocTarget.METHOD),
        @Description(value = STATUS_200_OK, target = DocTarget.RESPONSE)
    })
    public ObjectSet updateObjectSet(@PathParam("id") String id,
                                     ObjectSet objectSet) {
        return service.updateObjectSet(id, objectSet);
    }

    @DELETE
    @Path("{id}")
    @Descriptions({
        @Description(value = "Deletes an object set", target = DocTarget.METHOD),
        @Description(value = STATUS_204_NO_CONTENT, target = DocTarget.RESPONSE)
    })
    public void deleteObjectSet(@PathParam("id") String id) {
        service.deleteObjectSet(id);
    }

}
