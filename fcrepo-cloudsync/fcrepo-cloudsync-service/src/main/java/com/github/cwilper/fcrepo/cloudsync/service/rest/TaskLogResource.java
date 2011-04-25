package com.github.cwilper.fcrepo.cloudsync.service.rest;

import com.github.cwilper.fcrepo.cloudsync.api.CloudSyncService;
import org.apache.cxf.jaxrs.model.wadl.Description;
import org.apache.cxf.jaxrs.model.wadl.Descriptions;
import org.apache.cxf.jaxrs.model.wadl.DocTarget;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.io.InputStream;

@Path("tasklogs")
public class TaskLogResource extends AbstractResource {

    public TaskLogResource(CloudSyncService service) {
        super(service);
    }

    @GET
    @Path("/")
    @Produces({XML, JSON})
    @Descriptions({
        @Description(value = "Lists all task logs", target = DocTarget.METHOD),
        @Description(value = STATUS_200_OK, target = DocTarget.RESPONSE)
    })
    public TaskLogs listTaskLogs() {
        TaskLogs list = new TaskLogs();
        list.setTasklog(service.listTaskLogs());
        return list;
    }

    @GET
    @Path("{id}")
    @Produces({TEXT})
    @Descriptions({
        @Description(value = "Gets a task log", target = DocTarget.METHOD),
        @Description(value = STATUS_200_OK, target = DocTarget.RESPONSE)
    })
    public InputStream getTaskLog(@PathParam("id") String id) {
        return service.getTaskLog(id);
    }

    @DELETE
    @Path("{id}")
    @Descriptions({
        @Description(value = "Deletes a task log", target = DocTarget.METHOD),
        @Description(value = STATUS_204_NO_CONTENT, target = DocTarget.RESPONSE)
    })
    public void deleteTaskLog(@PathParam("id") String id) {
        deleteTaskLog(id);
    }

}
