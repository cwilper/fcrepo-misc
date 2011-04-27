package com.github.cwilper.fcrepo.cloudsync.api;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="user")
public class User {

    public String id;
    public String name;

}
