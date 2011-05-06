package com.github.cwilper.fcrepo.cloudsync.service.backend;

import com.github.cwilper.fcrepo.cloudsync.api.Task;

import java.util.Set;

public abstract class TaskRunner {

    public static final TaskRunner getInstance(Task task) {
        if (task.getType() != null && task.getType().length() > 0) {
            if (task.getType().equals("list")) {
                return new ListTaskRunner(task);
            } else {
                throw new IllegalArgumentException("Unrecognized Task type: " + task.getType());
            }
        } else {
            throw new IllegalArgumentException("Task type not specified");
        }
    }

    public abstract Set<Integer> getRelatedSetIds();

    public abstract Set<Integer> getRelatedStoreIds();
}
