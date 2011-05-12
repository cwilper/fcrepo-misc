package com.github.cwilper.fcrepo.cloudsync.service.backend;

import com.github.cwilper.fcrepo.cloudsync.api.ObjectStore;
import com.github.cwilper.fcrepo.cloudsync.service.util.JSON;
import com.github.cwilper.fcrepo.cloudsync.service.util.StringUtil;
import com.github.cwilper.fcrepo.dto.core.Datastream;
import com.github.cwilper.fcrepo.dto.core.DatastreamVersion;
import com.github.cwilper.fcrepo.dto.core.FedoraObject;

import java.io.InputStream;
import java.util.Map;

public class FedoraConnector extends StoreConnector {

    private final ObjectStore store;
    private final String url;
    private final String username;
    private final String password;

    public FedoraConnector(ObjectStore store) {
        this.store = store;
        Map<String, String> map = JSON.getMap(JSON.parse(store.getData()));
        url = StringUtil.validate("url", map.get("url"));
        username = StringUtil.validate("username", map.get("username"));
        password = StringUtil.validate("password", map.get("password"));
    }

    @Override
    public void countObjects(ObjectQuery query, ObjectCountHandler handler) {
    }

    @Override
    public void listObjects(ObjectQuery query, ObjectListHandler handler) {
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
