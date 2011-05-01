package com.github.cwilper.fcrepo.cloudsync.api;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="objectstore")
public class ObjectStore {

    public String id;
    public String name;
    public String type;
    public String data;

}
