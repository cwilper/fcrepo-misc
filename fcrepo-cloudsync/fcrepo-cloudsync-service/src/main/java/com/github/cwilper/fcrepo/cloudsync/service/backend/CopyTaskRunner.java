package com.github.cwilper.fcrepo.cloudsync.service.backend;

import com.github.cwilper.fcrepo.cloudsync.api.ObjectInfo;
import com.github.cwilper.fcrepo.cloudsync.api.Task;
import com.github.cwilper.fcrepo.cloudsync.service.dao.ObjectSetDao;
import com.github.cwilper.fcrepo.cloudsync.service.dao.ObjectStoreDao;
import com.github.cwilper.fcrepo.cloudsync.service.dao.TaskDao;
import com.github.cwilper.fcrepo.cloudsync.service.util.JSON;
import com.github.cwilper.fcrepo.cloudsync.service.util.StringUtil;
import com.github.cwilper.fcrepo.dto.core.ControlGroup;
import com.github.cwilper.fcrepo.dto.core.Datastream;
import com.github.cwilper.fcrepo.dto.core.FedoraObject;

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

    private StoreConnector queryConnector;
    private StoreConnector sourceConnector;
    private StoreConnector destConnector;

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
        queryConnector = StoreConnector.getInstance(objectStoreDao.getObjectStore("" + queryStoreId));
        sourceConnector = StoreConnector.getInstance(objectStoreDao.getObjectStore("" + sourceStoreId));
        destConnector = StoreConnector.getInstance(objectStoreDao.getObjectStore("" + destStoreId));
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
        FedoraObject o = sourceConnector.getObject(pid);
        if (o != null) {
            if (countManagedDatastreams(o) > 0) {
                logWriter.println("SKIPPED (managed datastream copying NOT IMPLEMENTED)");
            } else if (destConnector.putObject(o, overwrite)) {
                if (overwrite) {
                    logWriter.println("REPLACED (object existed in destination)");
                } else {
                    logWriter.println("SKIPPED (object existed in destination)");
                }
            } else {
                logWriter.println("OK (object is new in destination)");
            }
        } else {
            logWriter.println("SKIPPED (object does not exist in source)");
        }
    }

    private static int countManagedDatastreams(FedoraObject o) {
        int count = 0;
        for (Datastream ds: o.datastreams().values()) {
            if (ds.controlGroup().equals(ControlGroup.MANAGED)) {
                count++;
            }
        }
        return count;
    }

}
