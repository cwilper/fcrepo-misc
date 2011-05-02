package com.github.cwilper.fcrepo.cloudsync.service.backend;

import com.github.cwilper.fcrepo.cloudsync.api.ObjectStore;
import com.github.cwilper.fcrepo.dto.core.Datastream;
import com.github.cwilper.fcrepo.dto.core.DatastreamVersion;
import com.github.cwilper.fcrepo.dto.core.FedoraObject;

import java.io.InputStream;

public class DuraCloudConnector extends StoreConnector {

    private final ObjectStore store;

    public DuraCloudConnector(ObjectStore store) {
        this.store = store;
        // TODO: validate store fields, including json data
    }

    @Override
    public void countObjects(ObjectQuery query, ObjectCountHandler handler) {
    }

    @Override
    public void listObjects(ObjectQuery query, ObjectListHandler handler) {
        // https://demo.duracloud.org/durastore/cwilper-test?storeID=0
    }

    @Override
    public FedoraObject getObject(String pid) {
        return null;
    }

    @Override
    public InputStream getContent(FedoraObject o, Datastream ds, DatastreamVersion dsv) {
        // https://demo.duracloud.org/durastore/cwilper-test/content-id?storeID=0
        // note: if content-id has slashes, they should not be URL-encoded
        return null;
    }

}
