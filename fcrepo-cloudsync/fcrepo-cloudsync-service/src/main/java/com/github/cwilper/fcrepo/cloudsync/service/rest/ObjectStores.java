package com.github.cwilper.fcrepo.cloudsync.service.rest;

import com.github.cwilper.fcrepo.cloudsync.api.ObjectStore;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "objectstores")
public class ObjectStores {

    private List<ObjectStore> list;

    public void setObjectstore(List<ObjectStore> list) {
        this.list = list;
    }

    public List<ObjectStore> getObjectstore() {
        return list;
    }
}
