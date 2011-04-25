package com.github.cwilper.fcrepo.cloudsync.service.rest;

import com.github.cwilper.fcrepo.cloudsync.api.Task;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "tasks")
public class Tasks {

    private List<Task> list;

    public void setTask(List<Task> list) {
        this.list = list;
    }

    public List<Task> getTask() {
        return list;
    }
}
