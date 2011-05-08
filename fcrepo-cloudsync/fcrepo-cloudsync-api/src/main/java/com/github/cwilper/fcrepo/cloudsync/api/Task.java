package com.github.cwilper.fcrepo.cloudsync.api;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="task")
public class Task {

    private String id;
    private String name;
    private String type;
    private String state;
    private String activeLogId;
    private String schedule;
    private String data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getActiveLogId() {
        return activeLogId;
    }

    public void setActiveLogId(String activeLogId) {
        this.activeLogId = activeLogId;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
