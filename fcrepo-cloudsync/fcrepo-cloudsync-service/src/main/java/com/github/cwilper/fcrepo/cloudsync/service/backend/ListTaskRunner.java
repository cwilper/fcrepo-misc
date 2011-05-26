package com.github.cwilper.fcrepo.cloudsync.service.backend;

import com.github.cwilper.fcrepo.cloudsync.api.ObjectInfo;
import com.github.cwilper.fcrepo.cloudsync.api.Task;
import com.github.cwilper.fcrepo.cloudsync.service.dao.ObjectSetDao;
import com.github.cwilper.fcrepo.cloudsync.service.dao.ObjectStoreDao;
import com.github.cwilper.fcrepo.cloudsync.service.dao.TaskDao;
import com.github.cwilper.fcrepo.cloudsync.service.util.JSON;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ListTaskRunner extends TaskRunner implements ObjectListHandler {

    private final Integer setId;
    private final Integer storeId;

    private final Set<Integer> relatedSetIds = new HashSet<Integer>();
    private final Set<Integer> relatedStoreIds = new HashSet<Integer>();

    private TaskCanceledException canceledException;

    public ListTaskRunner(Task task,
                          TaskDao taskDao,
                          ObjectSetDao objectSetDao,
                          ObjectStoreDao objectStoreDao,
                          PrintWriter logWriter,
                          TaskCompletionListener completionListener) {
        super(task, taskDao, objectSetDao, objectStoreDao, logWriter, completionListener);
        Map<String, String> map = JSON.getMap(JSON.parse(task.getData()));
        setId = Integer.parseInt(map.get("setId"));
        storeId = Integer.parseInt(map.get("storeId"));
        relatedSetIds.add(setId);
        relatedStoreIds.add(storeId);
    }

    @Override
    public void runTask() throws Exception {
        StoreConnector connector = StoreConnector.getInstance(objectStoreDao.getObjectStore("" + storeId));
        try {
            ObjectQuery query = new ObjectQuery(objectSetDao.getObjectSet("" + setId));
            connector.listObjects(query, this);
            if (canceledException != null) {
                throw canceledException;
            }
        } finally {
            connector.close();
        }
    }

    @Override
    public Set<Integer> getRelatedSetIds() {
        return relatedSetIds;
    }

    @Override
    public Set<Integer> getRelatedStoreIds() {
        return relatedStoreIds;
    }

    // ObjectListHandler

    @Override
    public boolean handleObject(ObjectInfo info) {
        logWriter.println(info.getPid());
        try {
            pauseOrCancelIfRequested();
            return true;
        } catch (TaskCanceledException e) {
            canceledException = e;
            return false;
        }
    }

}
