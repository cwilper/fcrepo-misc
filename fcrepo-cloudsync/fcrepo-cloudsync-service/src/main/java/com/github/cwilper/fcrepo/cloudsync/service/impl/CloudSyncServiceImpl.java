package com.github.cwilper.fcrepo.cloudsync.service.impl;

import com.github.cwilper.fcrepo.cloudsync.api.CloudSyncService;
import com.github.cwilper.fcrepo.cloudsync.api.Configuration;
import com.github.cwilper.fcrepo.cloudsync.api.ObjectInfo;
import com.github.cwilper.fcrepo.cloudsync.api.ObjectSet;
import com.github.cwilper.fcrepo.cloudsync.api.ObjectStore;
import com.github.cwilper.fcrepo.cloudsync.api.SystemLog;
import com.github.cwilper.fcrepo.cloudsync.api.Task;
import com.github.cwilper.fcrepo.cloudsync.api.TaskLog;
import com.github.cwilper.fcrepo.cloudsync.api.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.List;

public class CloudSyncServiceImpl implements CloudSyncService {

    private static final Logger logger = LoggerFactory.getLogger(CloudSyncServiceImpl.class);

    private final JdbcTemplate db;

    private final ConfigurationDao configurationDao;
    private final UserDao userDao;
    private final TaskDao taskDao;
    private final ObjectSetDao objectSetDao;
    private final ObjectStoreDao objectStoreDao;
    private final SystemLogDao systemLogDao;
    private final TaskLogDao taskLogDao;

    public CloudSyncServiceImpl(DataSource dataSource) {
        db = new JdbcTemplate(dataSource);

        configurationDao = new ConfigurationDao(db);
        userDao = new UserDao(db);
        taskDao = new TaskDao(db);
        objectSetDao = new ObjectSetDao(db);
        objectStoreDao = new ObjectStoreDao(db);
        systemLogDao = new SystemLogDao(db);
        taskLogDao = new TaskLogDao(db);

        if (db.queryForInt("select count(*) from sys.systables where tablename = 'CLOUDSYNC'") == 0) {
            initDb();
        }
        logger.info("Service initialization complete. Ready to handle requests.");
    }

    private void initDb() {
        logger.info("First run detected. Creating database tables.");
        db.execute("create table CloudSync(schemaVersion int)");
        db.update("insert into CloudSync values (1)");
        configurationDao.initDb();
        userDao.initDb();
        taskDao.initDb();
        objectSetDao.initDb();
        objectStoreDao.initDb();
        systemLogDao.initDb();
        taskLogDao.initDb();
    }

    // -----------------------------------------------------------------------
    //                            Configuration
    // -----------------------------------------------------------------------

    @Override
    public Configuration getConfiguration() {
        return configurationDao.getConfiguration();
    }

    @Override
    public Configuration updateConfiguration(Configuration configuration) {
        return configurationDao.updateConfiguration(configuration);
    }

    // -----------------------------------------------------------------------
    //                                Users
    // -----------------------------------------------------------------------

    // return null if the user name conflicts with an existing one
    @Override
    public User createUser(User user) {
        return userDao.createUser(user);
    }

    @Override
    public List<User> listUsers() {
        return userDao.listUsers();
    }

    // return null if user not found
    @Override
    public User getUser(String id) {
        return userDao.getUser(id);
    }

    // return null if user not found
    @Override
    public User getCurrentUser() {
        return userDao.getCurrentUser();
    }

    // return null if user not found
    @Override
    public User updateUser(String id, User user) {
        return userDao.updateUser(id, user);
    }

    // no error if user not found
    @Override
    public void deleteUser(String id) {
        userDao.deleteUser(id);
    }

    // -----------------------------------------------------------------------
    //                                Tasks
    // -----------------------------------------------------------------------

    @Override
    public Task createTask(Task task) {
        return taskDao.createTask(task);
    }

    @Override
    public List<Task> listTasks() {
        return taskDao.listTasks();
    }

    @Override
    public Task getTask(String id) {
        return taskDao.getTask(id);
    }

    @Override
    public Task updateTask(String id, Task task) {
        return taskDao.updateTask(id, task);
    }

    @Override
    public void deleteTask(String id) {
        taskDao.deleteTask(id);
    }

    // -----------------------------------------------------------------------
    //                             Object Sets
    // -----------------------------------------------------------------------

    @Override
    public ObjectSet createObjectSet(ObjectSet objectSet) {
        return objectSetDao.createObjectSet(objectSet);
    }

    @Override
    public List<ObjectSet> listObjectSets() {
        return objectSetDao.listObjectSets();
    }

    @Override
    public ObjectSet getObjectSet(String id) {
        return objectSetDao.getObjectSet(id);
    }

    @Override
    public ObjectSet updateObjectSet(String id, ObjectSet objectSet) {
        return objectSetDao.updateObjectSet(id, objectSet);
    }

    @Override
    public void deleteObjectSet(String id) {
        objectSetDao.deleteObjectSet(id);
    }

    // -----------------------------------------------------------------------
    //                            Object Stores
    // -----------------------------------------------------------------------

    @Override
    public ObjectStore createObjectStore(ObjectStore objectStore) {
        return objectStoreDao.createObjectStore(objectStore);
    }

    @Override
    public List<ObjectStore> listObjectStores() {
        return objectStoreDao.listObjectStores();
    }

    @Override
    public ObjectStore getObjectStore(String id) {
        return objectStoreDao.getObjectStore(id);
    }

    @Override
    public List<ObjectInfo> queryObjectStore(String id, String set, long limit, long offset) {
        return objectStoreDao.queryObjectStore(id, set, limit, offset);
    }

    @Override
    public ObjectStore updateObjectStore(String id, ObjectStore objectStore) {
        return objectStoreDao.updateObjectStore(id, objectStore);
    }

    @Override
    public void deleteObjectStore(String id) {
        objectStoreDao.deleteObjectStore(id);
    }

    // -----------------------------------------------------------------------
    //                             System Logs
    // -----------------------------------------------------------------------

    @Override
    public List<SystemLog> listSystemLogs() {
        return systemLogDao.listSystemLogs();
    }

    @Override
    public SystemLog getSystemLog(String id) {
        return systemLogDao.getSystemLog(id);
    }

    @Override
    public InputStream getSystemLogContent(String id) {
        return systemLogDao.getSystemLogContent(id);
    }

    @Override
    public void deleteSystemLog(String id) {
        systemLogDao.deleteSystemLog(id);
    }

    // -----------------------------------------------------------------------
    //                              Task Logs
    // -----------------------------------------------------------------------

    @Override
    public List<TaskLog> listTaskLogs() {
        return taskLogDao.listTaskLogs();
    }

    @Override
    public TaskLog getTaskLog(String id) {
        return taskLogDao.getTaskLog(id);
    }

    @Override
    public InputStream getTaskLogContent(String id) {
        return taskLogDao.getTaskLogContent(id);
    }

    @Override
    public void deleteTaskLog(String id) {
        taskLogDao.deleteTaskLog(id);
    }
}
