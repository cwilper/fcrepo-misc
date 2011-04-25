package com.github.cwilper.fcrepo.cloudsync.service.rest;

import com.github.cwilper.fcrepo.cloudsync.api.ObjectInfo;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "objects")
public class Objects {

    private List<ObjectInfo> list;

    public void setObject(List<ObjectInfo> list) {
        this.list = list;
    }

    public List<ObjectInfo> getObject() {
        return list;
    }
}
