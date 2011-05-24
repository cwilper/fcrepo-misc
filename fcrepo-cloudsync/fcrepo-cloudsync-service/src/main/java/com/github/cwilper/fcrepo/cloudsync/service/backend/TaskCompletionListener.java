package com.github.cwilper.fcrepo.cloudsync.service.backend;

import com.github.cwilper.fcrepo.cloudsync.api.Task;

import java.util.Date;

public interface TaskCompletionListener {

    Date taskSucceeded(Task task);
    Date taskFailed(Task task, Throwable cause);
    Date taskCanceled(Task task);

}
