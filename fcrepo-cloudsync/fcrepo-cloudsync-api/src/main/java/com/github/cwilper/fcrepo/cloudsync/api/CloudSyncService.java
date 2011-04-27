package com.github.cwilper.fcrepo.cloudsync.api;

import java.io.InputStream;
import java.util.List;

public interface CloudSyncService {

    // -----------------------------------------------------------------------
    //                            Configuration
    // -----------------------------------------------------------------------

    Configuration getConfiguration();

    Configuration updateConfiguration(Configuration configuration);

    // -----------------------------------------------------------------------
    //                                Users
    // -----------------------------------------------------------------------

    User createUser(User user);

    List<User> listUsers();

    User getUser(String id);

    User getCurrentUser();

    User updateUser(String id, User user);

    void deleteUser(String id);

    // -----------------------------------------------------------------------
    //                                Tasks
    // -----------------------------------------------------------------------

    Task createTask(Task task);

    List<Task> listTasks();

    Task getTask(String id);

    Task updateTask(String id, Task task);

    void deleteTask(String id);

    // -----------------------------------------------------------------------
    //                             Object Sets
    // -----------------------------------------------------------------------

    ObjectSet createObjectSet(ObjectSet objectSet);

    List<ObjectSet> listObjectSets();

    ObjectSet getObjectSet(String id);

    ObjectSet updateObjectSet(String id, ObjectSet objectSet);

    void deleteObjectSet(String id);

    // -----------------------------------------------------------------------
    //                            Object Stores
    // -----------------------------------------------------------------------

    ObjectStore createObjectStore(ObjectStore objectStore);

    List<ObjectStore> listObjectStores();

    ObjectStore getObjectStore(String id);

    List<ObjectInfo> queryObjectStore(String id, String set, long limit, long offset);

    ObjectStore updateObjectStore(String id, ObjectStore objectStore);

    void deleteObjectStore(String id);

    // -----------------------------------------------------------------------
    //                             System Logs
    // -----------------------------------------------------------------------

    List<SystemLog> listSystemLogs();

    SystemLog getSystemLog(String id);

    InputStream getSystemLogContent(String id);

    void deleteSystemLog(String id);

    // -----------------------------------------------------------------------
    //                              Task Logs
    // -----------------------------------------------------------------------

    List<TaskLog> listTaskLogs();

    TaskLog getTaskLog(String id);

    InputStream getTaskLogContent(String id);

    void deleteTaskLog(String id);

}
