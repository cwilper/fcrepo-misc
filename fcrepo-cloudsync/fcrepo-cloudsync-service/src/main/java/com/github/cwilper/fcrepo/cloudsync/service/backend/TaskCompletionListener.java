package com.github.cwilper.fcrepo.cloudsync.service.backend;

import com.github.cwilper.fcrepo.cloudsync.api.Task;

public interface TaskCompletionListener {

    void taskSucceeded(Task task);
    void taskFailed(Task task, Throwable cause);
    void taskCanceled(Task task);

}
