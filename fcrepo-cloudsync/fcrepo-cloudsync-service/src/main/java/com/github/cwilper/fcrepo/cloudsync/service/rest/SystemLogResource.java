package com.github.cwilper.fcrepo.cloudsync.service.rest;

import com.github.cwilper.fcrepo.cloudsync.api.CloudSyncService;
import com.github.cwilper.fcrepo.cloudsync.api.SystemLog;
import org.apache.cxf.jaxrs.model.wadl.Description;
import org.apache.cxf.jaxrs.model.wadl.Descriptions;
import org.apache.cxf.jaxrs.model.wadl.DocTarget;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.io.InputStream;

@Path("systemlogs")
public class SystemLogResource extends AbstractResource {

    public SystemLogResource(CloudSyncService service) {
        super(service);
    }

    @GET
    @Path("/")
    @Produces({XML, JSON})
    @Descriptions({
        @Description(value = "Lists all system logs", target = DocTarget.METHOD),
        @Description(value = STATUS_200_OK, target = DocTarget.RESPONSE)
    })
    public SystemLogs listSystemLogs() {
        SystemLogs list = new SystemLogs();
        list.setSystemlog(service.listSystemLogs());
        return list;
    }

    @GET
    @Path("{id}")
    @Produces({XML, JSON})
    @Descriptions({
            @Description(value = "Gets a system log", target = DocTarget.METHOD),
            @Description(value = STATUS_200_OK, target = DocTarget.RESPONSE)
    })
    public SystemLog getSystemLog(@PathParam("id") String id) {
        return service.getSystemLog(id);
    }

    @GET
    @Path("{id}/content")
    @Produces({TEXT})
    @Descriptions({
        @Description(value = "Gets a system log's content", target = DocTarget.METHOD),
        @Description(value = STATUS_200_OK, target = DocTarget.RESPONSE)
    })
    public InputStream getSystemLogContent(@PathParam("id") String id) {
        return service.getSystemLogContent(id);
    }

    @DELETE
    @Path("{id}")
    @Descriptions({
        @Description(value = "Deletes a system log", target = DocTarget.METHOD),
        @Description(value = STATUS_204_NO_CONTENT, target = DocTarget.RESPONSE)
    })
    public void deleteSystemLog(@PathParam("id") String id) {
        service.deleteSystemLog(id);
    }

}
