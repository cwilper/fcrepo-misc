package com.github.cwilper.fcrepo.cloudsync.service.impl;

import com.github.cwilper.fcrepo.cloudsync.api.ObjectSet;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

class ObjectSetDao {

    private final JdbcTemplate db;

    public ObjectSetDao(JdbcTemplate db) {
        this.db = db;
    }

    public ObjectSet createObjectSet(ObjectSet objectSet) {
        return objectSet;
    }

    public List<ObjectSet> listObjectSets() {
        List<ObjectSet> list = new ArrayList<ObjectSet>();
        ObjectSet item = new ObjectSet();
        item.id = "1";
        list.add(item);
        return list;
    }

    public ObjectSet getObjectSet(String id) {
        ObjectSet item = new ObjectSet();
        item.id = "1";
        return item;
    }

    public ObjectSet updateObjectSet(String id, ObjectSet objectSet) {
        return objectSet;
    }

    public void deleteObjectSet(String id) {
    }

}