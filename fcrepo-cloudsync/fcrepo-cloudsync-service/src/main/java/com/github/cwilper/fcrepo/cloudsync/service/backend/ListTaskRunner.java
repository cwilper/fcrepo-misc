package com.github.cwilper.fcrepo.cloudsync.service.backend;

import com.github.cwilper.fcrepo.cloudsync.api.Task;
import com.github.cwilper.fcrepo.cloudsync.service.dao.ObjectSetDao;
import com.github.cwilper.fcrepo.cloudsync.service.dao.ObjectStoreDao;
import com.github.cwilper.fcrepo.cloudsync.service.dao.TaskDao;
import com.github.cwilper.fcrepo.cloudsync.service.util.JSON;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ListTaskRunner extends TaskRunner {

    private final Integer setId;
    private final Integer storeId;

    private final Set<Integer> relatedSetIds = new HashSet<Integer>();
    private final Set<Integer> relatedStoreIds = new HashSet<Integer>();

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
        // Fake running a task that takes 10 seconds or so, with an
        // opportunity to pause or cancel halfway through.
        logWriter.println("Hi. I'm the ListTaskRunner, and I'm running.");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        pauseOrCancelIfRequested();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        logWriter.println("Hi. I'm the ListTaskRunner, and I'm done.");
    }

    @Override
    public Set<Integer> getRelatedSetIds() {
        return relatedSetIds;
    }

    @Override
    public Set<Integer> getRelatedStoreIds() {
        return relatedStoreIds;
    }
}
