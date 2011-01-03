package com.github.cwilper.fcrepo.dto.core;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A Datastream within a <code>FedoraObject</code>.
 */
public class Datastream extends FedoraDTO {

    private final SortedSet<DatastreamVersion> versions =
            new TreeSet<DatastreamVersion>(new DSVComparator());

    private final String id;
    
    private State state;
    private ControlGroup controlGroup;
    private Boolean versionable;

    /**
     * Creates an instance.
     *
     * @param id the id of the datastream (not null, immutable).
     * @throws NullPointerException if id is given as <code>null</code>.
     */
    public Datastream(String id) {
        this.id = Util.normalize(id);
        if (this.id == null) {
            throw new NullPointerException();
        }
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
    Object[] getEqArray() {
        return new Object[] { id, state, controlGroup, versionable,
                versions };
    }

    private static class DSVComparator
            implements Comparator<DatastreamVersion>, Serializable {

        @Override
        public int compare(DatastreamVersion a, DatastreamVersion b) {
            Date aDate = a.createdDate();
            Date bDate = b.createdDate();
            if (aDate == null) {
                if (bDate == null) {
                    return b.id().compareTo(a.id());
                } else {
                    return -1;
                }
            } else {
                if (bDate == null) {
                    return 1;
                } else {
                    return bDate.compareTo(aDate);
                }
            }
        }
    }

}
