package com.github.cwilper.fcrepo.cloudsync.service.backend;

import com.github.cwilper.fcrepo.cloudsync.api.ObjectStore;
import com.github.cwilper.fcrepo.dto.core.Datastream;
import com.github.cwilper.fcrepo.dto.core.DatastreamVersion;
import com.github.cwilper.fcrepo.dto.core.FedoraObject;

import java.io.InputStream;

public class DuraCloudObjectStoreConnector extends ObjectStoreConnector {

    private final ObjectStore store;

    public DuraCloudObjectStoreConnector(ObjectStore store) {
        this.store = store;
        // TODO: validate store fields, including json data
    }

    @Override
    public void countObjects(ObjectQuery query, ObjectCountResultHandler handler) {
    }

    @Override
    public void listObjects(ObjectQuery query, ObjectListResultHandler handler) {
    }

    @Override
    public FedoraObject getObject(String pid) {
        return null;
    }

    @Override
    public InputStream getContent(FedoraObject o, Datastream ds, DatastreamVersion dsv) {
        return null;
    }
}
