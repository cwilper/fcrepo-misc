package com.github.cwilper.fcrepo.cloudsync.service.rest;

import com.github.cwilper.fcrepo.cloudsync.api.CloudSyncService;
import com.github.cwilper.fcrepo.cloudsync.api.TaskLog;
import org.apache.cxf.jaxrs.model.wadl.Description;
import org.apache.cxf.jaxrs.model.wadl.Descriptions;
import org.apache.cxf.jaxrs.model.wadl.DocTarget;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.io.InputStream;
import java.util.List;

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
    public List<TaskLog> listTaskLogs() {
        return service.listTaskLogs();
    }

    @GET
    @Path("{id}")
    @Produces({XML, JSON})
    @Descriptions({
            @Description(value = "Gets a task log", target = DocTarget.METHOD),
            @Description(value = STATUS_200_OK, target = DocTarget.RESPONSE)
    })
    public TaskLog getTaskLog(@PathParam("id") String id) {
        return service.getTaskLog(id);
    }

    @GET
    @Path("{id}/content")
    @Produces({TEXT})
    @Descriptions({
        @Description(value = "Gets a task log's content", target = DocTarget.METHOD),
        @Description(value = STATUS_200_OK, target = DocTarget.RESPONSE)
    })
    public InputStream getTaskLogContent(@PathParam("id") String id) {
        return service.getTaskLogContent(id);
    }

    @DELETE
    @Path("{id}")
    @Descriptions({
        @Description(value = "Deletes a task log", target = DocTarget.METHOD),
        @Description(value = STATUS_204_NO_CONTENT, target = DocTarget.RESPONSE)
    })
    public void deleteTaskLog(@PathParam("id") String id) {
        service.deleteTaskLog(id);
    }

}
