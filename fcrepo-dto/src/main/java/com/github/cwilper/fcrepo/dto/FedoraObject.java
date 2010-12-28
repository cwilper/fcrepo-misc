package com.github.cwilper.fcrepo.dto;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class FedoraObject {

    private final SortedMap<String, Datastream> datastreams =
            new TreeMap<String, Datastream>();

    private String pid;
    private State state;
    private String label;
    private String ownerId;
    private Date createdDate;
    private Date lastModifiedDate;

    public FedoraObject() {
    }

    public String pid() {
        return pid;
    }

    public FedoraObject pid(String pid) {
        this.pid = pid;
        return this;
    }

    public State state() {
        return state;
    }

    public FedoraObject state(State state) {
        this.state = state;
        return this;
    }

    public String label() {
        return label;
    }

    public FedoraObject label(String label) {
        this.label = label;
        return this;
    }

    public String ownerId() {
        return ownerId;
    }

    public FedoraObject ownerId(String ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public Date createdDate() {
        return createdDate;
    }

    public FedoraObject createdDate(Date createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public Date lastModifiedDate() {
        return lastModifiedDate;
    }

    public FedoraObject lastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public SortedMap<String, Datastream> datastreams() {
        return datastreams;
    }

    public void write(OutputStream sink,
                      Set<String> managedDatastreamsToEmbed,
                      boolean includeFedoraURIs) {
        // TODO: Serialize!
    }

    public FedoraObject read(InputStream source) {
        // TODO: Deserialize!
        return null;
    }

}
