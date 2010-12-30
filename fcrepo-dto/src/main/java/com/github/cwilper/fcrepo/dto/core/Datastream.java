package com.github.cwilper.fcrepo.dto.core;

import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

public class Datastream {

    private final SortedSet<DatastreamVersion> versions =
            new TreeSet<DatastreamVersion>();

    private String id;
    private State state;
    private ControlGroup controlGroup;
    private Boolean versionable;

    public Datastream() {
    }

    public String id() {
        return id;
    }

    public Datastream id(String id) {
        this.id = id;
        return this;
    }

    public State state() {
        return state;
    }

    public Datastream state(State state) {
        this.state = state;
        return this;
    }

    public ControlGroup controlGroup() {
        return controlGroup;
    }

    public Datastream controlGroup(ControlGroup controlGroup) {
        this.controlGroup = controlGroup;
        return this;
    }

    public Boolean versionable() {
        return versionable;
    }

    public Datastream versionable(Boolean versionable) {
        this.versionable = versionable;
        return this;
    }

    public SortedSet<DatastreamVersion> versions() {
        return versions;
    }

    @Override
    public final int hashCode() {
        return Util.computeHash(getEqArray());
    }

    @Override
    public final boolean equals(Object o) {
        return o instanceof Datastream && Arrays.equals(
                ((Datastream) o).getEqArray(), getEqArray());
    }

    Object[] getEqArray() {
        return new Object[] { id, state, controlGroup, versionable, versions };
    }

}
