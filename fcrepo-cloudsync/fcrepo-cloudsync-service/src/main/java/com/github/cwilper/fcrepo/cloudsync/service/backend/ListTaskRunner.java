package com.github.cwilper.fcrepo.cloudsync.service.backend;

import com.github.cwilper.fcrepo.cloudsync.api.Task;
import com.github.cwilper.fcrepo.cloudsync.service.util.JSON;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ListTaskRunner extends TaskRunner {

    private final Task task;
    private final Integer setId;
    private final Integer storeId;

    private final Set<Integer> relatedSetIds = new HashSet<Integer>();
    private final Set<Integer> relatedStoreIds = new HashSet<Integer>();

    public ListTaskRunner(Task task) {
        this.task = task;
        Map<String, String> map = JSON.getMap(JSON.parse(task.getData()));
        setId = Integer.parseInt(map.get("setId"));
        storeId = Integer.parseInt(map.get("storeId"));
        relatedSetIds.add(setId);
        relatedStoreIds.add(storeId);
    }

    @Override
    public Set<Integer> getRelatedSetIds() {
        return null;
    }

    @Override
    public Set<Integer> getRelatedStoreIds() {
        return null;
    }
}
