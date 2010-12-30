package com.github.cwilper.fcrepo.dto.core;

import java.util.Arrays;
import java.util.Date;
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
    
}
