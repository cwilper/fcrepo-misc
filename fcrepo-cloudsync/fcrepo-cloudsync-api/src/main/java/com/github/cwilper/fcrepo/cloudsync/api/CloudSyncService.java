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

    User createUser(User user) throws NameConflictException;

    List<User> listUsers();

    User getUser(String id);

    User getCurrentUser();

    User updateUser(String id, User user) throws NameConflictException;

    void deleteUser(String id);

    // -----------------------------------------------------------------------
    //                                Tasks
    // -----------------------------------------------------------------------

    Task createTask(Task task) throws NameConflictException;

    List<Task> listTasks();

    Task getTask(String id);

    Task updateTask(String id, Task task) throws NameConflictException;

    void deleteTask(String id);

    // -----------------------------------------------------------------------
    //                             Object Sets
    // -----------------------------------------------------------------------

    ObjectSet createObjectSet(ObjectSet objectSet) throws NameConflictException;

    List<ObjectSet> listObjectSets();

    ObjectSet getObjectSet(String id);

    ObjectSet updateObjectSet(String id, ObjectSet objectSet) throws NameConflictException;

    void deleteObjectSet(String id);

    // -----------------------------------------------------------------------
    //                            Object Stores
    // -----------------------------------------------------------------------

    ObjectStore createObjectStore(ObjectStore objectStore) throws NameConflictException;

    List<ObjectStore> listObjectStores();

    ObjectStore getObjectStore(String id);

    List<ObjectInfo> queryObjectStore(String id, String set, long limit, long offset);

    ObjectStore updateObjectStore(String id, ObjectStore objectStore) throws NameConflictException;

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

    // -----------------------------------------------------------------------
    //                             DuraCloud
    // -----------------------------------------------------------------------

    List<ProviderAccount> listProviderAccounts(String url,
                                               String username,
                                               String password);

    List<Space> listSpaces(String url,
                           String username,
                           String password,
                           String providerAccountId);

}
