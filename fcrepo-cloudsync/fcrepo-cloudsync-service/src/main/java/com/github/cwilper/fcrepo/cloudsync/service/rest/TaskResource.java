package com.github.cwilper.fcrepo.cloudsync.service.rest;

import com.github.cwilper.fcrepo.cloudsync.api.CloudSyncService;
import com.github.cwilper.fcrepo.cloudsync.api.NameConflictException;
import com.github.cwilper.fcrepo.cloudsync.api.ResourceInUseException;
import com.github.cwilper.fcrepo.cloudsync.api.ResourceNotFoundException;
import com.github.cwilper.fcrepo.cloudsync.api.Task;
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

@Path("tasks")
public class TaskResource extends AbstractResource {

    public TaskResource(CloudSyncService service) {
        super(service);
    }

    @POST
    @Path("/")
    @Consumes({XML, JSON})
    @Descriptions({
        @Description(value = "Creates a task", target = DocTarget.METHOD),
        @Description(value = STATUS_201_CREATED, target = DocTarget.RESPONSE)
    })
    public Response createTask(@Context UriInfo uriInfo,
                               Task task) {
        try {
            Task newTask = service.createTask(task);
            URI uri = getResourceURI(uriInfo.getRequestUri(), newTask.getId());
            return Response.created(uri).entity(newTask).build();
        } catch (NameConflictException e) {
            throw new WebApplicationException(e, Response.Status.CONFLICT);
        }
    }

    @GET
    @Path("/")
    @Produces({XML, JSON})
    @Descriptions({
        @Description(value = "Lists all tasks", target = DocTarget.METHOD),
        @Description(value = STATUS_200_OK, target = DocTarget.RESPONSE)
    })
    public List<Task> listTasks() {
        return service.listTasks();
    }

    @GET
    @Path("{id}")
    @Produces({XML, JSON})
    @Descriptions({
        @Description(value = "Gets a task", target = DocTarget.METHOD),
        @Description(value = STATUS_200_OK, target = DocTarget.RESPONSE)
    })
    public Task getTask(@PathParam("id") String id) {
        try {
            return service.getTask(id);
        } catch (ResourceNotFoundException e) {
            throw new WebApplicationException(e, Response.Status.NOT_FOUND);
        }
    }

    @PUT
    @Path("{id}")
    @Consumes({XML, JSON})
    @Produces({XML, JSON})
    @Descriptions({
        @Description(value = "Updates a task", target = DocTarget.METHOD),
        @Description(value = STATUS_200_OK, target = DocTarget.RESPONSE)
    })
    public Task updateTask(@PathParam("id") String id,
                           Task task) {
        try {
            return service.updateTask(id, task);
        } catch (ResourceNotFoundException e) {
            throw new WebApplicationException(e, Response.Status.NOT_FOUND);
        } catch (NameConflictException e) {
            throw new WebApplicationException(e, Response.Status.CONFLICT);
        }
    }

    @DELETE
    @Path("{id}")
    @Descriptions({
        @Description(value = "Deletes a task", target = DocTarget.METHOD),
        @Description(value = STATUS_204_NO_CONTENT, target = DocTarget.RESPONSE)
    })
    public void deleteTask(@PathParam("id") String id) {
        try {
            service.deleteTask(id);
        } catch (ResourceInUseException e) {
            throw new WebApplicationException(e, Response.Status.CONFLICT);
        }
    }

}
