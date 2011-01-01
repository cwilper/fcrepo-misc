package com.github.cwilper.fcrepo.dto.core;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

public class Datastream {

    private final SortedSet<DatastreamVersion> versions =
            new TreeSet<DatastreamVersion>(new DSVComparator());

    private final String id;
    
    private State state;
    private ControlGroup controlGroup;
    private Boolean versionable;

    public Datastream(String id) {
        if (id == null) {
            throw new NullPointerException();
        }
        this.id = id;
    }

    public String id() {
        return id;
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

    public DatastreamVersion addVersion() {
        return addVersion(null);
    }

    public DatastreamVersion addVersion(Date createdDate) {
        int n = versions.size();
        while (hasVersion(id + "." + n)) {
            n++;
        }
        DatastreamVersion dsv = new DatastreamVersion(id + "." + n,
                createdDate);
        versions.add(dsv);
        return dsv;
    }

    private boolean hasVersion(String id) {
        for (DatastreamVersion dsv: versions) {
            if (dsv.id().equals(id)) return true;
        }
        return false;
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
        return new Object[] { id, state, controlGroup, versionable,
                versions };
    }

    private class DSVComparator implements Comparator<DatastreamVersion> {

        @Override
        public int compare(DatastreamVersion a, DatastreamVersion b) {
            Date aDate = a.createdDate();
            Date bDate = b.createdDate();
            if (aDate == null) {
                if (bDate == null) {
                    return a.id().compareTo(b.id());
                } else {
                    return 1;
                }
            } else {
                if (bDate == null) {
                    return -1;
                } else {
                    return aDate.compareTo(bDate);
                }
            }
        }
    }

}
