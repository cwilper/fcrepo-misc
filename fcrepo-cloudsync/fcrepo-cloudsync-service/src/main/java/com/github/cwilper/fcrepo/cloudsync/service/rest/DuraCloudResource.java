package com.github.cwilper.fcrepo.cloudsync.service.rest;

import com.github.cwilper.fcrepo.cloudsync.api.CloudSyncService;
import com.github.cwilper.fcrepo.cloudsync.api.ProviderAccount;
import com.github.cwilper.fcrepo.cloudsync.api.Space;
import org.apache.cxf.jaxrs.model.wadl.Description;
import org.apache.cxf.jaxrs.model.wadl.Descriptions;
import org.apache.cxf.jaxrs.model.wadl.DocTarget;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.List;

@Path("duracloud")
public class DuraCloudResource extends AbstractResource {

    public DuraCloudResource(CloudSyncService service) {
        super(service);
    }

    @GET
    @Path("/provideraccounts")
    @Consumes({XML, JSON})
    @Descriptions({
            @Description(value = "Lists the Storage Provider Accounts configured for a DuraCloud Instance", target = DocTarget.METHOD),
            @Description(value = STATUS_200_OK, target = DocTarget.RESPONSE)
    })
    public List<ProviderAccount> listProviderAccounts(
            @QueryParam("url") String url,
            @QueryParam("username") String username,
            @QueryParam("password") String password) {
        return service.listProviderAccounts(url, username, password);
    }

    @GET
    @Path("/spaces")
    @Consumes({XML, JSON})
    @Descriptions({
        @Description(value = "Lists the Spaces available within a Storage Provider Account on a DuraCloud Instance", target = DocTarget.METHOD),
        @Description(value = STATUS_200_OK, target = DocTarget.RESPONSE)
    })
    public List<Space> listSpaces(
            @QueryParam("url") String url,
            @QueryParam("username") String username,
            @QueryParam("password") String password,
            @QueryParam("providerAccountId") String providerAccountId) {
        return service.listSpaces(url, username, password, providerAccountId);
    }

}
