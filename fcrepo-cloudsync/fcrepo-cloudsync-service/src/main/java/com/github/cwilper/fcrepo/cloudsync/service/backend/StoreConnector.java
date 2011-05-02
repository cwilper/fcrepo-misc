package com.github.cwilper.fcrepo.cloudsync.service.backend;

import com.github.cwilper.fcrepo.cloudsync.api.ObjectStore;
import com.github.cwilper.fcrepo.dto.core.Datastream;
import com.github.cwilper.fcrepo.dto.core.DatastreamVersion;
import com.github.cwilper.fcrepo.dto.core.FedoraObject;

import java.io.InputStream;

public abstract class StoreConnector {

    public static final StoreConnector getInstance(ObjectStore store) {
        if (store.getType() != null && store.getType().length() > 0) {
            if (store.getType().equals("fedora")) {
                return new FedoraConnector(store);
            } else if (store.getType().equals("duracloud")) {
                return new DuraCloudConnector(store);
            } else {
                throw new IllegalArgumentException("Unrecognized ObjectStore type: " + store.getType());
            }
        } else {
            throw new IllegalArgumentException("ObjectStore type not specified");
        }
    }

    public abstract void countObjects(ObjectQuery query,
                                      ObjectCountHandler handler);

    public abstract void listObjects(ObjectQuery query,
                                     ObjectListHandler handler);

    public abstract FedoraObject getObject(String pid);

    public abstract InputStream getContent(FedoraObject o,
                                           Datastream ds,
                                           DatastreamVersion dsv);

}
