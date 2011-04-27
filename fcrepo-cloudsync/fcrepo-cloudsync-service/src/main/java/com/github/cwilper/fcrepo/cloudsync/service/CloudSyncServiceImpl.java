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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class CloudSyncServiceImpl implements CloudSyncService {

    private static final Logger logger = LoggerFactory.getLogger(CloudSyncServiceImpl.class);

    private final JdbcTemplate db;

    public CloudSyncServiceImpl(DataSource dataSource) {
        db = new JdbcTemplate(dataSource);
        if (db.queryForInt("select count(*) from sys.systables where tablename = 'CLOUDSYNC'") == 0) {
            initDb();
        }
        logger.info("Service initialization complete. Ready to handle requests.");
    }

    private void initDb() {
        logger.info("First run detected. Creating database tables.");

        db.execute("create table CloudSync(schemaVersion int)");
        db.update("insert into CloudSync values (1)");

        db.execute("create table Configuration(keepSysLogDays int, keepTaskLogDays int)");
        db.execute("insert into Configuration values (-1, -1)");
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
        return configuration;
    }

    // -----------------------------------------------------------------------
    //                                Users
    // -----------------------------------------------------------------------

    @Override
    public User createUser(User user) {
        return user;
    }

    @Override
    public List<User> listUsers() {
        List<User> list = new ArrayList<User>();
        User user = new User();
        user.id = "1";
        list.add(user);
        return list;
    }

    @Override
    public User getUser(String id) {
        User user = new User();
        user.id = id;
        return user;
    }

    @Override
    public User getCurrentUser() {
        User user = new User();
        user.id = "9";
        org.springframework.security.core.userdetails.User u =
                (org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.name = u.getUsername();
        return user;
    }

    @Override
    public User updateUser(String id, User user) {
        User u = new User();
        u.id = user.id;
        return u;
    }

    @Override
    public void deleteUser(String id) {
    }

    // -----------------------------------------------------------------------
    //                                Tasks
    // -----------------------------------------------------------------------

    @Override
    public Task createTask(Task task) {
        return task;
    }

    @Override
    public List<Task> listTasks() {
        List<Task> list = new ArrayList<Task>();
        Task item = new Task();
        item.id = "1";
        list.add(item);
        return list;
    }

    @Override
    public Task getTask(String id) {
        Task item = new Task();
        item.id = id;
        return item;
    }

    @Override
    public Task updateTask(String id, Task task) {
        return task;
    }

    @Override
    public void deleteTask(String id) {
    }

    // -----------------------------------------------------------------------
    //                             Object Sets
    // -----------------------------------------------------------------------

    @Override
    public ObjectSet createObjectSet(ObjectSet objectSet) {
        return objectSet;
    }

    @Override
    public List<ObjectSet> listObjectSets() {
        List<ObjectSet> list = new ArrayList<ObjectSet>();
        ObjectSet item = new ObjectSet();
        item.id = "1";
        list.add(item);
        return list;
    }

    @Override
    public ObjectSet getObjectSet(String id) {
        ObjectSet item = new ObjectSet();
        item.id = "1";
        return item;
    }

    @Override
    public ObjectSet updateObjectSet(String id, ObjectSet objectSet) {
        return objectSet;
    }

    @Override
    public void deleteObjectSet(String id) {
    }

    // -----------------------------------------------------------------------
    //                            Object Stores
    // -----------------------------------------------------------------------

    @Override
    public ObjectStore createObjectStore(ObjectStore objectStore) {
        return objectStore;
    }

    @Override
    public List<ObjectStore> listObjectStores() {
        List<ObjectStore> list = new ArrayList<ObjectStore>();
        ObjectStore item = new ObjectStore();
        item.id = "1";
        list.add(item);
        return list;
    }

    @Override
    public ObjectStore getObjectStore(String id) {
        ObjectStore item = new ObjectStore();
        item.id = id;
        return item;
    }

    @Override
    public List<ObjectInfo> queryObjectStore(String id, String set, long limit, long offset) {
        List<ObjectInfo> list = new ArrayList<ObjectInfo>();
        ObjectInfo item = new ObjectInfo();
        item.pid = "test:object1";
        list.add(item);
        return list;
    }

    @Override
    public ObjectStore updateObjectStore(String id, ObjectStore objectStore) {
        return objectStore;
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
        item.id = "1";
        list.add(item);
        return list;
    }

    @Override
    public SystemLog getSystemLog(String id) {
        SystemLog item = new SystemLog();
        item.id = id;
        return item;
    }

    @Override
    public InputStream getSystemLogContent(String id) {
        try {
            return new ByteArrayInputStream(("System log " + id + " content").getBytes("UTF-8"));
        } catch (UnsupportedEncodingException wontHappen) {
            throw new RuntimeException(wontHappen);
        }
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
        item.id = "1";
        list.add(item);
        return list;
    }

    @Override
    public TaskLog getTaskLog(String id) {
        TaskLog item = new TaskLog();
        item.id = id;
        return item;
    }

    @Override
    public InputStream getTaskLogContent(String id) {
        try {
            return new ByteArrayInputStream(("Task log " + id + " content").getBytes("UTF-8"));
        } catch (UnsupportedEncodingException wontHappen) {
            throw new RuntimeException(wontHappen);
        }
    }

    @Override
    public void deleteTaskLog(String id) {
    }
}
