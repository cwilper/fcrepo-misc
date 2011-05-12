package com.github.cwilper.fcrepo.cloudsync.service.backend;

import com.github.cwilper.fcrepo.cloudsync.api.ObjectStore;
import com.github.cwilper.fcrepo.cloudsync.service.util.JSON;
import com.github.cwilper.fcrepo.cloudsync.service.util.StringUtil;
import com.github.cwilper.fcrepo.dto.core.Datastream;
import com.github.cwilper.fcrepo.dto.core.DatastreamVersion;
import com.github.cwilper.fcrepo.dto.core.FedoraObject;

import java.io.InputStream;
import java.util.Map;

public class DuraCloudConnector extends StoreConnector {

    private final ObjectStore store;
    private final String url;
    private final String username;
    private final String password;
    private final String providerId;
    private final String providerName;
    private final String space;
    private final String prefix;

    public DuraCloudConnector(ObjectStore store) {
        this.store = store;
        Map<String, String> map = JSON.getMap(JSON.parse(store.getData()));
        url = StringUtil.validate("url", map.get("url"));
        username = StringUtil.validate("username", map.get("username"));
        password = StringUtil.validate("password", map.get("password"));
        providerId = StringUtil.validate("providerId", map.get("providerId"));
        providerName = StringUtil.validate("providerName", map.get("providerName"));
        space = StringUtil.validate("space", map.get("space"));
        prefix = StringUtil.normalize(map.get("prefix"));
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
