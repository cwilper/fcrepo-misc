package com.github.cwilper.fcrepo.dto.core;

import java.util.Arrays;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A Fedora Digital Object.
 */
public class FedoraObject {

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
        if (createdDate == null) {
            return null;
        } else {
            return new Date(createdDate.getTime());
        }
    }

    public FedoraObject createdDate(Date createdDate) {
        if (createdDate == null) {
            this.createdDate = null;
        } else {
            this.createdDate = new Date(createdDate.getTime());
        }
        return this;
    }

    public Date lastModifiedDate() {
        if (lastModifiedDate == null) {
            return null;
        } else {
            return new Date(lastModifiedDate.getTime());
        }
    }

    public FedoraObject lastModifiedDate(Date lastModifiedDate) {
        if (lastModifiedDate == null) {
            this.lastModifiedDate = null;
        } else {
            this.lastModifiedDate = new Date(lastModifiedDate.getTime());
        }
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
    public final int hashCode() {
        return Util.computeHash(getEqArray());
    }

    @Override
    public final boolean equals(Object o) {
        return o instanceof FedoraObject && Arrays.equals(
                ((FedoraObject) o).getEqArray(), getEqArray());
    }

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
