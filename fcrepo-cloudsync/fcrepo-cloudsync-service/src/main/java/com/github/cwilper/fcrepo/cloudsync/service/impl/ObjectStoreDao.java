package com.github.cwilper.fcrepo.cloudsync.service.impl;

import com.github.cwilper.fcrepo.cloudsync.api.ObjectInfo;
import com.github.cwilper.fcrepo.cloudsync.api.ObjectStore;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

class ObjectStoreDao extends AbstractDao {

    public ObjectStoreDao(JdbcTemplate db) {
        super(db);
    }

    @Override
    public void initDb() {
        // TODO: Implement me
    }

    public ObjectStore createObjectStore(ObjectStore objectStore) {
        return objectStore;
    }

    public List<ObjectStore> listObjectStores() {
        List<ObjectStore> list = new ArrayList<ObjectStore>();
        ObjectStore item = new ObjectStore();
        item.id = "1";
        list.add(item);
        return list;
    }

    public ObjectStore getObjectStore(String id) {
        ObjectStore item = new ObjectStore();
        item.id = id;
        return item;
    }

    public List<ObjectInfo> queryObjectStore(String id, String set, long limit, long offset) {
        List<ObjectInfo> list = new ArrayList<ObjectInfo>();
        ObjectInfo item = new ObjectInfo();
        item.pid = "test:object1";
        list.add(item);
        return list;
    }

    public ObjectStore updateObjectStore(String id, ObjectStore objectStore) {
        return objectStore;
    }

    public void deleteObjectStore(String id) {
    }

}