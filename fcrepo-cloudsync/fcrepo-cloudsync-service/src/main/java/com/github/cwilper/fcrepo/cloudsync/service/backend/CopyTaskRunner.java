package com.github.cwilper.fcrepo.cloudsync.service.backend;

import com.github.cwilper.fcrepo.cloudsync.api.ObjectInfo;
import com.github.cwilper.fcrepo.cloudsync.api.Task;
import com.github.cwilper.fcrepo.cloudsync.service.dao.ObjectSetDao;
import com.github.cwilper.fcrepo.cloudsync.service.dao.ObjectStoreDao;
import com.github.cwilper.fcrepo.cloudsync.service.dao.TaskDao;
import com.github.cwilper.fcrepo.cloudsync.service.util.JSON;
import com.github.cwilper.fcrepo.cloudsync.service.util.StringUtil;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CopyTaskRunner extends TaskRunner implements ObjectListHandler {

    private final Integer setId;
    private final Integer queryStoreId;
    private final Integer sourceStoreId;
    private final Integer destStoreId;
    private final boolean overwrite;

    private final Set<Integer> relatedSetIds = new HashSet<Integer>();
    private final Set<Integer> relatedStoreIds = new HashSet<Integer>();

    private TaskCanceledException canceledException;

    public CopyTaskRunner(Task task,
                          TaskDao taskDao,
                          ObjectSetDao objectSetDao,
                          ObjectStoreDao objectStoreDao,
                          PrintWriter logWriter,
                          TaskCompletionListener completionListener) {
        super(task, taskDao, objectSetDao, objectStoreDao, logWriter, completionListener);
        Map<String, String> map = JSON.getMap(JSON.parse(task.getData()));
        setId = Integer.parseInt(map.get("setId"));
        queryStoreId = Integer.parseInt(map.get("queryStoreId"));
        sourceStoreId = Integer.parseInt(map.get("sourceStoreId"));
        destStoreId = Integer.parseInt(map.get("destStoreId"));
        overwrite = StringUtil.validate("overwrite", map.get("overwrite"), new String[] { "true", "false" }).equals("true");
        relatedSetIds.add(setId);
        relatedStoreIds.add(queryStoreId);
        relatedStoreIds.add(sourceStoreId);
        relatedStoreIds.add(destStoreId);
    }

    @Override
    public void runTask() throws Exception {
        StoreConnector queryConnector = StoreConnector.getInstance(objectStoreDao.getObjectStore("" + queryStoreId));
        StoreConnector sourceConnector = StoreConnector.getInstance(objectStoreDao.getObjectStore("" + sourceStoreId));
        StoreConnector destConnector = StoreConnector.getInstance(objectStoreDao.getObjectStore("" + destStoreId));
        try {
            ObjectQuery query = new ObjectQuery(objectSetDao.getObjectSet("" + setId));
            queryConnector.listObjects(query, this);
            if (canceledException != null) {
                throw canceledException;
            }
        } finally {
            queryConnector.close();
            sourceConnector.close();
            destConnector.close();
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
        logWriter.print("Copying " + info.getPid() + "..");
        try {
            pauseOrCancelIfRequested();
            doCopy(info.getPid());
            pauseOrCancelIfRequested();
            return true;
        } catch (TaskCanceledException e) {
            canceledException = e;
            return false;
        }
    }

    private void doCopy(String pid) {
        logWriter.println("NOT IMPLEMENTED");
    }

}
