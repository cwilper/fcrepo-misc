package com.github.cwilper.fcrepo.cloudsync.service.rest;

import com.github.cwilper.fcrepo.cloudsync.api.User;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "users")
public class Users {

    private List<User> list;

    public void setUser(List<User> list) {
        this.list = list;
    }

    public List<User> getUser() {
        return list;
    }
}
