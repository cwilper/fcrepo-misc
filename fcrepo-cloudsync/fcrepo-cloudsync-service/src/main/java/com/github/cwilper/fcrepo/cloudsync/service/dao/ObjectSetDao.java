package com.github.cwilper.fcrepo.cloudsync.service.dao;

import com.github.cwilper.fcrepo.cloudsync.api.ObjectSet;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class ObjectSetDao extends AbstractDao {

    public ObjectSetDao(JdbcTemplate db) {
        super(db);
    }

    @Override
    public void initDb() {
        // TODO: implement me
    }

    public ObjectSet createObjectSet(ObjectSet objectSet) {
        return objectSet;
    }

    public List<ObjectSet> listObjectSets() {
        List<ObjectSet> list = new ArrayList<ObjectSet>();
        ObjectSet item = new ObjectSet();
        item.setId("1");
        list.add(item);
        return list;
    }

    public ObjectSet getObjectSet(String id) {
        ObjectSet item = new ObjectSet();
        item.setId("1");
        return item;
    }

    public ObjectSet updateObjectSet(String id, ObjectSet objectSet) {
        return objectSet;
    }

    public void deleteObjectSet(String id) {
    }

}