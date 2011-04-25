package com.github.cwilper.fcrepo.cloudsync.service.rest;

import com.github.cwilper.fcrepo.cloudsync.api.SystemLog;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "systemlogs")
public class SystemLogs {

    private List<SystemLog> list;

    public void setSystemlog(List<SystemLog> list) {
        this.list = list;
    }

    public List<SystemLog> getSystemlog() {
        return list;
    }
}
