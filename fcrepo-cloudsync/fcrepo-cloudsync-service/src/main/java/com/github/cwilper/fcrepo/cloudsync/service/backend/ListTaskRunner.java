package com.github.cwilper.fcrepo.cloudsync.service.backend;

import com.github.cwilper.fcrepo.cloudsync.api.Task;

import java.util.Set;

public class ListTaskRunner extends TaskRunner {

    private final Task task;

    public ListTaskRunner(Task task) {
        this.task = task;
        // TODO: validate task-specific stuff
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
