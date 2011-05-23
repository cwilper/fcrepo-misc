package com.github.cwilper.fcrepo.cloudsync.service.rest;

import com.github.cwilper.fcrepo.cloudsync.api.CloudSyncService;
import com.github.cwilper.fcrepo.cloudsync.api.NameConflictException;
import com.github.cwilper.fcrepo.cloudsync.api.ResourceInUseException;
import com.github.cwilper.fcrepo.cloudsync.api.ResourceNotFoundException;
import com.github.cwilper.fcrepo.cloudsync.api.User;
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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Path("users")
public class UserResource extends AbstractResource {

    public UserResource(CloudSyncService service) {
        super(service);
    }

    @POST
    @Path("/")
    @Consumes({XML, JSON})
    @Descriptions({
        @Description(value = "Creates a user", target = DocTarget.METHOD),
        @Description(value = STATUS_201_CREATED, target = DocTarget.RESPONSE)
    })
    public Response createUser(@Context UriInfo uriInfo,
                               User user) {
        try {
            User newUser = service.createUser(user);
            URI uri = getResourceURI(uriInfo.getRequestUri(), newUser.getId());
            return Response.created(uri).entity(newUser).build();
        } catch (NameConflictException e) {
            throw new WebApplicationException(e, Response.Status.CONFLICT);
        }
    }

    @GET
    @Path("/")
    @Produces({XML, JSON})
    @Descriptions({
        @Description(value = "Lists all users", target = DocTarget.METHOD),
        @Description(value = STATUS_200_OK, target = DocTarget.RESPONSE)
    })
    public List<User> listUsers() {
        return service.listUsers();
    }

    @GET
    @Path("{id}")
    @Produces({XML, JSON})
    @Descriptions({
            @Description(value = "Gets a user", target = DocTarget.METHOD),
            @Description(value = STATUS_200_OK, target = DocTarget.RESPONSE)
    })
    public User getUser(@PathParam("id") String id) {
        try {
            return service.getUser(id);
        } catch (ResourceNotFoundException e) {
            throw new WebApplicationException(e, Response.Status.NOT_FOUND);
        }
    }

    @GET
    @Path("current")
    @Produces({XML, JSON})
    @Descriptions({
        @Description(value = "Gets the currently logged in user", target = DocTarget.METHOD),
        @Description(value = STATUS_200_OK, target = DocTarget.RESPONSE)
    })
    public User getCurrentUser() {
        return service.getCurrentUser();
    }

    @PUT
    @Path("{id}")
    @Consumes({XML, JSON})
    @Produces({XML, JSON})
    @Descriptions({
        @Description(value = "Updates a user", target = DocTarget.METHOD),
        @Description(value = STATUS_200_OK, target = DocTarget.RESPONSE)
    })
    public User updateUser(@PathParam("id") String id,
                           User user) {
        try {
            return service.updateUser(id, user);
        } catch (ResourceNotFoundException e) {
            throw new WebApplicationException(e, Response.Status.NOT_FOUND);
        } catch (NameConflictException e) {
            throw new WebApplicationException(e, Response.Status.CONFLICT);
        }
    }

    @DELETE
    @Path("{id}")
    @Descriptions({
        @Description(value = "Deletes a user", target = DocTarget.METHOD),
        @Description(value = STATUS_204_NO_CONTENT, target = DocTarget.RESPONSE)
    })
    public void deleteUser(@PathParam("id") String id) {
        try {
            service.deleteUser(id);
        } catch (ResourceInUseException e) {
            throw new WebApplicationException(e, Response.Status.CONFLICT);
        }
    }

}
