package com.github.cwilper.fcrepo.cloudsync.service.dao;

import com.github.cwilper.fcrepo.cloudsync.api.Task;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class TaskDao extends AbstractDao {

    int num = 0;

    public TaskDao(JdbcTemplate db) {
        super(db);
    }

    @Override
    public void initDb() {
        // TODO: Implement me
    }

    public Task createTask(Task task) {
        return task;
    }

    public List<Task> listTasks() {
        List<Task> list = new ArrayList<Task>();
        for (int i = 0; i < num; i++ ) {
            Task item = new Task();
            item.setId("" + i);
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
        item.setId(id);
        return item;
    }

    public Task updateTask(String id, Task task) {
        return task;
    }

    public void deleteTask(String id) {
    }

}