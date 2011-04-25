package com.github.cwilper.fcrepo.cloudsync.service.rest;

import com.github.cwilper.fcrepo.cloudsync.api.TaskLog;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "tasklogs")
public class TaskLogs {

    private List<TaskLog> list;

    public void setTasklog(List<TaskLog> list) {
        this.list = list;
    }

    public List<TaskLog> getTasklog() {
        return list;
    }
}
