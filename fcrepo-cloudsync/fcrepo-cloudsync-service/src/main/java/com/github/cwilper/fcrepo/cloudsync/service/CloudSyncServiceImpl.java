package com.github.cwilper.fcrepo.cloudsync.service;

import com.github.cwilper.fcrepo.cloudsync.api.CloudSyncService;
import com.github.cwilper.fcrepo.cloudsync.api.Configuration;
import com.github.cwilper.fcrepo.cloudsync.api.NameConflictException;
import com.github.cwilper.fcrepo.cloudsync.api.ObjectInfo;
import com.github.cwilper.fcrepo.cloudsync.api.ObjectSet;
import com.github.cwilper.fcrepo.cloudsync.api.ObjectStore;
import com.github.cwilper.fcrepo.cloudsync.api.ProviderAccount;
import com.github.cwilper.fcrepo.cloudsync.api.Space;
import com.github.cwilper.fcrepo.cloudsync.api.SystemLog;
import com.github.cwilper.fcrepo.cloudsync.api.Task;
import com.github.cwilper.fcrepo.cloudsync.api.TaskLog;
import com.github.cwilper.fcrepo.cloudsync.api.User;
import com.github.cwilper.fcrepo.cloudsync.service.dao.ConfigurationDao;
import com.github.cwilper.fcrepo.cloudsync.service.dao.DuraCloudDao;
import com.github.cwilper.fcrepo.cloudsync.service.dao.ObjectSetDao;
import com.github.cwilper.fcrepo.cloudsync.service.dao.ObjectStoreDao;
import com.github.cwilper.fcrepo.cloudsync.service.dao.SystemLogDao;
import com.github.cwilper.fcrepo.cloudsync.service.dao.TaskDao;
import com.github.cwilper.fcrepo.cloudsync.service.dao.TaskLogDao;
import com.github.cwilper.fcrepo.cloudsync.service.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.List;

public class CloudSyncServiceImpl implements CloudSyncService {

    private static final Logger logger = LoggerFactory.getLogger(CloudSyncServiceImpl.class);

    private final JdbcTemplate db;
    private final TransactionTemplate tt;

    private final ConfigurationDao configurationDao;
    private final UserDao userDao;
    private final TaskDao taskDao;
    private final ObjectSetDao objectSetDao;
    private final ObjectStoreDao objectStoreDao;
    private final SystemLogDao systemLogDao;
    private final TaskLogDao taskLogDao;
    private final DuraCloudDao duraCloudDao;

    public CloudSyncServiceImpl(DataSource dataSource,
                                PlatformTransactionManager txMan) {
        db = new JdbcTemplate(dataSource);
        tt = new TransactionTemplate(txMan);

        configurationDao = new ConfigurationDao(db);
        userDao = new UserDao(db);
        taskDao = new TaskDao(db, tt);
        objectSetDao = new ObjectSetDao(db);
        objectStoreDao = new ObjectStoreDao(db);
        systemLogDao = new SystemLogDao(db);
        taskLogDao = new TaskLogDao(db);
        duraCloudDao = new DuraCloudDao();

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
        objectSetDao.initDb();
        objectStoreDao.initDb();
        taskDao.initDb();
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
    public User createUser(User user) throws NameConflictException {
        try {
            return userDao.createUser(user);
        } catch (DuplicateKeyException e) {
            throw new NameConflictException("User name is already in use", e);
        }
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
    public User updateUser(String id, User user)
            throws NameConflictException {
        try {
            return userDao.updateUser(id, user);
        } catch (DuplicateKeyException e) {
            throw new NameConflictException("User name is already in use", e);
        }
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
    public Task createTask(Task task) throws NameConflictException {
        try {
            return taskDao.createTask(task);
        } catch (DuplicateKeyException e) {
            throw new NameConflictException("Task name is already in use", e);
        }
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
    public Task updateTask(String id, Task task) throws NameConflictException {
        try {
            return taskDao.updateTask(id, task);
        } catch (DuplicateKeyException e) {
            throw new NameConflictException("Task name is already in use", e);
        }
    }

    @Override
    public void deleteTask(String id) {
        taskDao.deleteTask(id);
    }

    // -----------------------------------------------------------------------
    //                             Object Sets
    // -----------------------------------------------------------------------

    @Override
    public ObjectSet createObjectSet(ObjectSet objectSet)
            throws NameConflictException {
        try {
            return objectSetDao.createObjectSet(objectSet);
        } catch (DuplicateKeyException e) {
            throw new NameConflictException("Object Set name is already in use", e);
        }
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
    public ObjectSet updateObjectSet(String id, ObjectSet objectSet)
            throws NameConflictException {
        try {
            return objectSetDao.updateObjectSet(id, objectSet);
        } catch (DuplicateKeyException e) {
            throw new NameConflictException("Object Set name is already in use", e);
        }
    }

    @Override
    public void deleteObjectSet(String id) {
        objectSetDao.deleteObjectSet(id);
    }

    // -----------------------------------------------------------------------
    //                            Object Stores
    // -----------------------------------------------------------------------

    @Override
    public ObjectStore createObjectStore(ObjectStore objectStore)
            throws NameConflictException {
        try {
            return objectStoreDao.createObjectStore(objectStore);
        } catch (DuplicateKeyException e) {
            throw new NameConflictException("Object Store name is already in use", e);
        }
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
    public ObjectStore updateObjectStore(String id, ObjectStore objectStore)
            throws NameConflictException {
        try {
            return objectStoreDao.updateObjectStore(id, objectStore);
        } catch (DuplicateKeyException e) {
            throw new NameConflictException("Object Store name is already in use", e);
        }
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

    // -----------------------------------------------------------------------
    //                               DuraCloud
    // -----------------------------------------------------------------------

    @Override
    public List<ProviderAccount> listProviderAccounts(String url,
                                               String username,
                                               String password) {
        return duraCloudDao.listProviderAccounts(url, username, password);
    }

    @Override
    public List<Space> listSpaces(String url,
                           String username,
                           String password,
                           String providerAccountId) {
        return duraCloudDao.listSpaces(url, username, password, providerAccountId);
    }
}
