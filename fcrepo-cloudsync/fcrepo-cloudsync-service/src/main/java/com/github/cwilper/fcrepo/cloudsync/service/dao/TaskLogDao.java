package com.github.cwilper.fcrepo.cloudsync.service.dao;

import com.github.cwilper.fcrepo.cloudsync.api.TaskLog;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class TaskLogDao extends AbstractDao {

    public TaskLogDao(JdbcTemplate db) {
        super(db);
    }

    @Override
    public void initDb() {
        // TODO: Implement me
    }

    public List<TaskLog> listTaskLogs() {
        List<TaskLog> list = new ArrayList<TaskLog>();
        TaskLog item = new TaskLog();
        item.setId("1");
        list.add(item);
        return list;
    }

    public TaskLog getTaskLog(String id) {
        TaskLog item = new TaskLog();
        item.setId(id);
        return item;
    }

    public InputStream getTaskLogContent(String id) {
        try {
            return new ByteArrayInputStream(("Task log " + id + " content").getBytes("UTF-8"));
        } catch (UnsupportedEncodingException wontHappen) {
            throw new RuntimeException(wontHappen);
        }
    }

    public void deleteTaskLog(String id) {
    }

}