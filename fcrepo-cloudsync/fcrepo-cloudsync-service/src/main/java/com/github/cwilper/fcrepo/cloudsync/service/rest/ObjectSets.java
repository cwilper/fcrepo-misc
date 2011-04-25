package com.github.cwilper.fcrepo.cloudsync.service.rest;

import com.github.cwilper.fcrepo.cloudsync.api.ObjectSet;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "objectsets")
public class ObjectSets {

    private List<ObjectSet> list;

    public void setObjectset(List<ObjectSet> list) {
        this.list = list;
    }

    public List<ObjectSet> getObjectset() {
        return list;
    }
}
