package com.github.cwilper.fcrepo.cloudsync.service.rest;

import com.github.cwilper.fcrepo.cloudsync.api.CloudSyncService;
import com.github.cwilper.fcrepo.cloudsync.api.NameConflictException;
import com.github.cwilper.fcrepo.cloudsync.api.ObjectInfo;
import com.github.cwilper.fcrepo.cloudsync.api.ObjectStore;
import com.github.cwilper.fcrepo.cloudsync.api.ResourceInUseException;
import com.github.cwilper.fcrepo.cloudsync.api.ResourceNotFoundException;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Path("objectstores")
public class ObjectStoreResource extends AbstractResource {

    public ObjectStoreResource(CloudSyncService service) {
        super(service);
    }

    @POST
    @Path("/")
    @Consumes({XML, JSON})
    @Descriptions({
        @Description(value = "Creates an object store", target = DocTarget.METHOD),
        @Description(value = STATUS_201_CREATED, target = DocTarget.RESPONSE)
    })
    public Response createObjectStore(@Context UriInfo uriInfo,
                                      ObjectStore objectStore) {
        try {
            ObjectStore newObjectStore = service.createObjectStore(objectStore);
            URI uri = getResourceURI(uriInfo.getRequestUri(), newObjectStore.getId());
            return Response.created(uri).entity(newObjectStore).build();
        } catch (NameConflictException e) {
            throw new WebApplicationException(e, Response.Status.CONFLICT);
        }
    }

    @GET
    @Path("/")
    @Produces({XML, JSON})
    @Descriptions({
        @Description(value = "Lists all object stores", target = DocTarget.METHOD),
        @Description(value = STATUS_200_OK, target = DocTarget.RESPONSE)
    })
    public List<ObjectStore> listObjectStores() {
        return service.listObjectStores();
    }

    @GET
    @Path("{id}")
    @Produces({XML, JSON})
    @Descriptions({
            @Description(value = "Gets an object store", target = DocTarget.METHOD),
            @Description(value = STATUS_200_OK, target = DocTarget.RESPONSE)
    })
    public ObjectStore getObjectStore(@PathParam("id") String id) {
        try {
            return service.getObjectStore(id);
        } catch (ResourceNotFoundException e) {
            throw new WebApplicationException(e, Response.Status.NOT_FOUND);
        }
    }

    @GET
    @Path("{id}/objects")
    @Produces({XML, JSON})
    @Descriptions({
        @Description(value = "Queries an object store", target = DocTarget.METHOD),
        @Description(value = STATUS_200_OK, target = DocTarget.RESPONSE)
    })
    public List<ObjectInfo> queryObjectStore(@PathParam("id") String id,
                                     @QueryParam("set") String set,
                                     @QueryParam("limit") long limit,
                                     @QueryParam("offset") long offset) {
        return service.queryObjectStore(id, set, limit, offset);
    }

    @PUT
    @Path("{id}")
    @Consumes({XML, JSON})
    @Produces({XML, JSON})
    @Descriptions({
        @Description(value = "Updates an object store", target = DocTarget.METHOD),
        @Description(value = STATUS_200_OK, target = DocTarget.RESPONSE)
    })
    public ObjectStore updateObjectStore(@PathParam("id") String id,
                                         ObjectStore objectStore) {
        try {
            return service.updateObjectStore(id, objectStore);
        } catch (ResourceNotFoundException e) {
            throw new WebApplicationException(e, Response.Status.NOT_FOUND);
        } catch (NameConflictException e) {
            throw new WebApplicationException(e, Response.Status.CONFLICT);
        }
    }

    @DELETE
    @Path("{id}")
    @Descriptions({
        @Description(value = "Deletes an object store", target = DocTarget.METHOD),
        @Description(value = STATUS_204_NO_CONTENT, target = DocTarget.RESPONSE)
    })
    public void deleteObjectStore(@PathParam("id") String id) {
        try {
            service.deleteObjectStore(id);
        } catch (ResourceInUseException e) {
            throw new WebApplicationException(e, Response.Status.CONFLICT);
        }
    }

}
