package com.github.cwilper.fcrepo.cloudsync.service.backend;

import com.github.cwilper.fcrepo.cloudsync.api.Task;
import com.github.cwilper.fcrepo.cloudsync.service.dao.ObjectSetDao;
import com.github.cwilper.fcrepo.cloudsync.service.dao.ObjectStoreDao;
import com.github.cwilper.fcrepo.cloudsync.service.dao.TaskDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.util.Set;

public abstract class TaskRunner extends Thread {

    private final Logger logger = LoggerFactory.getLogger(TaskRunner.class);

    protected final Task task;
    protected final TaskDao taskDao;
    protected final ObjectSetDao objectSetDao;
    protected final ObjectStoreDao objectStoreDao;
    protected final PrintWriter logWriter;
    protected final TaskCompletionListener completionListener;

    protected final Schedule schedule;

    protected boolean pauseRequested;
    protected boolean resumeRequested;
    protected boolean cancelRequested;

    protected TaskRunner(Task task,
                         TaskDao taskDao,
                         ObjectSetDao objectSetDao,
                         ObjectStoreDao objectStoreDao,
                         PrintWriter logWriter,
                         TaskCompletionListener completionListener) {
        this.task = task;
        this.taskDao = taskDao;
        this.objectSetDao = objectSetDao;
        this.objectStoreDao = objectStoreDao;
        this.logWriter = logWriter;
        this.completionListener = completionListener;
        if (task.getSchedule() != null) {
            this.schedule = new Schedule(task.getSchedule());
        } else {
            this.schedule = null;
        }
    }

    public static final TaskRunner getInstance(Task task,
                                               TaskDao taskDao,
                                               ObjectSetDao objectSetDao,
                                               ObjectStoreDao objectStoreDao,
                                               PrintWriter logWriter,
                                               TaskCompletionListener completionListener) {
        if (task.getType() != null && task.getType().length() > 0) {
            if (task.getType().equals("list")) {
                return new ListTaskRunner(task, taskDao, objectSetDao, objectStoreDao, logWriter, completionListener);
            } else {
                throw new IllegalArgumentException("Unrecognized Task type: " + task.getType());
            }
        } else {
            throw new IllegalArgumentException("Task type not specified");
        }
    }

    @Override
    public final void run() {
        try {
            logger.info("Task " + task.getId() + " started (" + task.getName() + ")");
            pauseOrCancelIfRequested();
            runTask();
            pauseOrCancelIfRequested();
            logger.info("Task " + task.getId() + " succeeded (" + task.getName() + ")");
            completionListener.taskSucceeded(task);
        } catch (TaskCanceledException e) {
            logger.info("Task " + task.getId() + " canceled (" + task.getName() + ")");
            completionListener.taskCanceled(task);
        } catch (Throwable th) {
            logger.info("Task " + task.getId() + " failed (" + task.getName() + ")");
            completionListener.taskFailed(task, th);
        } finally {
            logWriter.close();
        }
    }

    protected abstract void runTask() throws Exception;

    protected void pauseOrCancelIfRequested() throws TaskCanceledException {
        if (cancelRequested) {
            throw new TaskCanceledException();
        } else if (pauseRequested) {
            taskDao.setTaskState(task.getId(), Task.PAUSED);
            logger.info("Task " + task.getId() + " paused (" + task.getName() + ")");
            while (!resumeRequested) {
                if (cancelRequested) {
                    throw new TaskCanceledException();
                }
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                }
            }
            taskDao.setTaskState(task.getId(), Task.RUNNING);
            logger.info("Task " + task.getId() + " resumed (" + task.getName() + ")");
            pauseRequested = false;
            resumeRequested = false;
        }
    }

    public Task getTask() {
        return task;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void requestPause() {
        pauseRequested = true;
        resumeRequested = false;
        cancelRequested = false;
    }

    public void requestResume() {
        pauseRequested = false;
        resumeRequested = true;
        cancelRequested = false;
    }

    public void requestCancel() {
        pauseRequested = false;
        resumeRequested = false;
        cancelRequested = true;
    }

    public abstract Set<Integer> getRelatedSetIds();

    public abstract Set<Integer> getRelatedStoreIds();
}
