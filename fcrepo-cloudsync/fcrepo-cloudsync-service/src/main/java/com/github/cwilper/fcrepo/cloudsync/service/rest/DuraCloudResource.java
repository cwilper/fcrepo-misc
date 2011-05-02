package com.github.cwilper.fcrepo.cloudsync.service.rest;

import com.github.cwilper.fcrepo.cloudsync.api.CloudSyncService;
import com.github.cwilper.fcrepo.cloudsync.api.ProviderAccount;
import org.apache.cxf.jaxrs.model.wadl.Description;
import org.apache.cxf.jaxrs.model.wadl.Descriptions;
import org.apache.cxf.jaxrs.model.wadl.DocTarget;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
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
            @QueryParam("url") String hostname,
            @QueryParam("username") String username,
            @QueryParam("password") String password) {
        // https://demo.duracloud.org/durastore/stores
        // response: xml
        List<ProviderAccount> list = new ArrayList<ProviderAccount>();
        ProviderAccount account1 = new ProviderAccount();
        account1.setId("0");
        account1.setType("AMAZON_S3");
        account1.setPrimary(true);
        list.add(account1);
        ProviderAccount account2 = new ProviderAccount();
        account2.setId("1");
        account2.setType("MICROSOFT_AZURE");
        list.add(account2);
        return list;
    }

    @GET
    @Path("/stores")
    @Consumes({XML, JSON})
    @Descriptions({
        @Description(value = "Lists the Spaces available within a Storage Provider Account on a DuraCloud Instance", target = DocTarget.METHOD),
        @Description(value = STATUS_200_OK, target = DocTarget.RESPONSE)
    })
    public List<Space> listSpaces(
            @QueryParam("url") String hostname,
            @QueryParam("username") String username,
            @QueryParam("password") String password,
            @QueryParam("providerAccountId") String providerAccountId) {
        // https://demo.duracloud.org/durastore/spaces?storeID=1
        List<Space> list = new ArrayList<Space>();
        Space space1 = new Space(); space1.id = "space-one"; list.add(space1);
        Space space2 = new Space(); space2.id = "space-two"; list.add(space2);
        Space space3 = new Space(); space3.id = "space-three"; list.add(space3);
        return list;
    }

    @XmlRootElement(name="space")
    static class Space {
        public String id;
    }

}
