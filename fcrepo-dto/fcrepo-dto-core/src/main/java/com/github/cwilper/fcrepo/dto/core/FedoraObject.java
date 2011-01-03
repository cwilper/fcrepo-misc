package com.github.cwilper.fcrepo.dto.core;

import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A Fedora Digital Object.
 */
public class FedoraObject extends FedoraDTO {

    private final SortedMap<String, Datastream> datastreams = new DSMap();

    private String pid;
    private State state;
    private String label;
    private String ownerId;
    private Date createdDate;
    private Date lastModifiedDate;

    /**
     * Creates an instance.
     */
    public FedoraObject() {
    }

    public String pid() {
        return pid;
    }

    public FedoraObject pid(String pid) {
        this.pid = Util.normalize(pid);
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
        this.label = Util.normalize(label);
        return this;
    }

    public String ownerId() {
        return ownerId;
    }

    public FedoraObject ownerId(String ownerId) {
        this.ownerId = Util.normalize(ownerId);
        return this;
    }

    public Date createdDate() {
        return Util.copy(createdDate);
    }

    public FedoraObject createdDate(Date createdDate) {
        this.createdDate = Util.copy(createdDate);
        return this;
    }

    public Date lastModifiedDate() {
        return Util.copy(lastModifiedDate);
    }

    public FedoraObject lastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = Util.copy(lastModifiedDate);
        return this;
    }

    public FedoraObject putDatastream(Datastream ds) {
        datastreams.put(ds.id(), ds);
        return this;
    }

    public SortedMap<String, Datastream> datastreams() {
        return datastreams;
    }

    @Override
    Object[] getEqArray() {
        return new Object[] { pid, state, label, ownerId, createdDate,
                lastModifiedDate, datastreams };
    }

    private static class DSMap extends TreeMap<String, Datastream> {

        @Override
        public Datastream put(String id, Datastream datastream) {
            if (!datastream.id().equals(id)) {
                throw new IllegalArgumentException();
            }
            return super.put(id, datastream);
        }
        
    }

}
