package com.github.cwilper.fcrepo.cloudsync.service;

import com.github.cwilper.fcrepo.cloudsync.api.CloudSyncService;
import com.github.cwilper.fcrepo.cloudsync.api.Configuration;
import com.github.cwilper.fcrepo.cloudsync.api.ObjectInfo;
import com.github.cwilper.fcrepo.cloudsync.api.ObjectSet;
import com.github.cwilper.fcrepo.cloudsync.api.ObjectStore;
import com.github.cwilper.fcrepo.cloudsync.api.SystemLog;
import com.github.cwilper.fcrepo.cloudsync.api.Task;
import com.github.cwilper.fcrepo.cloudsync.api.TaskLog;
import com.github.cwilper.fcrepo.cloudsync.api.User;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CloudSyncServiceImpl implements CloudSyncService {

    private final File homeDir;

    public CloudSyncServiceImpl() {
        // TODO: Populate homeDir via:
        // 1 - cloudsync.home from cloudsync.properties
        // 2 - cloudsync.home system property
        // 3 - CLOUDSYNC_HOME environment variable
        homeDir = null;
    }

    // -----------------------------------------------------------------------
    //                            Configuration
    // -----------------------------------------------------------------------

    @Override
    public Configuration getConfiguration() {
        Configuration configuration = new Configuration();
        return configuration;
    }

    @Override
    public Configuration updateConfiguration(Configuration configuration) {
        return null;
    }

    // -----------------------------------------------------------------------
    //                                Users
    // -----------------------------------------------------------------------

    @Override
    public String createUser(User user) {
        return "1";
    }

    @Override
    public List<User> listUsers() {
        List<User> list = new ArrayList<User>();
        User user1 = new User();
        user1.setId("1");
        list.add(user1);
//        User user2 = new User();
//        user2.setId("2");
//        list.add(user2);
        return list;
    }

    @Override
    public User getUser(String id) {
        return null;
    }

    @Override
    public User updateUser(String id, User user) {
        return null;
    }

    @Override
    public void deleteUser(String id) {
    }

    // -----------------------------------------------------------------------
    //                                Tasks
    // -----------------------------------------------------------------------

    @Override
    public String createTask(Task task) {
        return null;
    }

    @Override
    public List<Task> listTasks() {
        List<Task> list = new ArrayList<Task>();
        Task item = new Task();
        item.setId("1");
        list.add(item);
        return list;
    }

    @Override
    public Task getTask(String id) {
        return null;
    }

    @Override
    public Task updateTask(String id, Task task) {
        return null;
    }

    @Override
    public void deleteTask(String id) {
    }

    // -----------------------------------------------------------------------
    //                             Object Sets
    // -----------------------------------------------------------------------

    @Override
    public String createObjectSet(ObjectSet objectSet) {
        return null;
    }

    @Override
    public List<ObjectSet> listObjectSets() {
        List<ObjectSet> list = new ArrayList<ObjectSet>();
        ObjectSet item = new ObjectSet();
        item.setId("1");
        list.add(item);
        return list;
    }

    @Override
    public ObjectSet getObjectSet(String id) {
        return null;
    }

    @Override
    public ObjectSet updateObjectSet(String id, ObjectSet objectSet) {
        return null;
    }

    @Override
    public void deleteObjectSet(String id) {
    }

    // -----------------------------------------------------------------------
    //                            Object Stores
    // -----------------------------------------------------------------------

    @Override
    public String createObjectStore(ObjectStore objectStore) {
        return null;
    }

    @Override
    public List<ObjectStore> listObjectStores() {
        List<ObjectStore> list = new ArrayList<ObjectStore>();
        ObjectStore item = new ObjectStore();
        item.setId("1");
        list.add(item);
        return list;
    }

    @Override
    public ObjectStore getObjectStore(String id) {
        return null;
    }

    @Override
    public List<ObjectInfo> queryObjectStore(String id, String set, long limit, long offset) {
        return null;
    }

    @Override
    public ObjectStore updateObjectStore(String id, ObjectStore objectStore) {
        return null;
    }

    @Override
    public void deleteObjectStore(String id) {
    }

    // -----------------------------------------------------------------------
    //                             System Logs
    // -----------------------------------------------------------------------

    @Override
    public List<SystemLog> listSystemLogs() {
        List<SystemLog> list = new ArrayList<SystemLog>();
        SystemLog item = new SystemLog();
        item.setId("1");
        list.add(item);
        return list;
    }

    @Override
    public InputStream getSystemLog(String id) {
        return null;
    }

    @Override
    public void deleteSystemLog(String id) {
    }

    // -----------------------------------------------------------------------
    //                              Task Logs
    // -----------------------------------------------------------------------

    @Override
    public List<TaskLog> listTaskLogs() {
        List<TaskLog> list = new ArrayList<TaskLog>();
        TaskLog item = new TaskLog();
        item.setId("1");
        list.add(item);
        return list;
    }

    @Override
    public InputStream getTaskLog(String id) {
        return null;
    }

    @Override
    public void deleteTaskLog(String id) {
    }
}
