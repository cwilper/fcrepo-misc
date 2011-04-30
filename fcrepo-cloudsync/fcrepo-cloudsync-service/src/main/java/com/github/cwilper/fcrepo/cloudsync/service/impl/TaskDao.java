package com.github.cwilper.fcrepo.cloudsync.service.impl;

import com.github.cwilper.fcrepo.cloudsync.api.Task;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

class TaskDao {

    int num = 0;

    private final JdbcTemplate db;

    public TaskDao(JdbcTemplate db) {
        this.db = db;
    }

    public Task createTask(Task task) {
        return task;
    }

    public List<Task> listTasks() {
        List<Task> list = new ArrayList<Task>();
        for (int i = 0; i < num; i++ ) {
            Task item = new Task();
            item.id = "" + i;
            list.add(item);
        }
        num++;
        if (num == 4) {
            num = 0;
        }
        return list;
    }

    public Task getTask(String id) {
        Task item = new Task();
        item.id = id;
        return item;
    }

    public Task updateTask(String id, Task task) {
        return task;
    }

    public void deleteTask(String id) {
    }

}