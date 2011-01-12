package com.github.cwilper.fcrepo.dto.core;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A Datastream within a <code>FedoraObject</code>.
 *
 * @see <a href="package-summary.html#working">Working With DTO Classes</a>
 */
public class Datastream extends FedoraDTO {

    private final SortedSet<DatastreamVersion> versions =
            new TreeSet<DatastreamVersion>(new DSVComparator());

    private final String id;
    
    private State state;
    private ControlGroup controlGroup;
    private Boolean versionable;

    /**
     * Creates an empty instance with an id.  An id is the only required
     * field of a datastream. It is immutable and must be provided at
     * construction time.
     *
     * @param id the id of the datastream (not null, immutable), which
     *        will be string-normalized.
     * @throws NullPointerException if the normalized id is <code>null</code>.
     */
    public Datastream(String id) {
        this.id = Util.normalize(id);
        if (this.id == null) {
            throw new NullPointerException();
        }
    }

    /**
     * Gets the id.
     *
     * @return the value, never <code>null</code>.
     */
    public String id() {
        return id;
    }

    /**
     * Gets the state.
     *
     * @return the state, or <code>null</code> if undefined.
     */
    public State state() {
        return state;
    }

    /**
     * Sets the state.
     *
     * @param state the new value, possibly <code>null</code>.
     * @return this instance.
     */
    public Datastream state(State state) {
        this.state = state;
        return this;
    }

    /**
     * Gets the control group.
     *
     * @return the value, or <code>null</code> if undefined.
     */
    public ControlGroup controlGroup() {
        return controlGroup;
    }

    /**
     * Sets the control group.
     *
     * @param controlGroup the new value, possibly <code>null</code>.
     * @return this instance.
     */
    public Datastream controlGroup(ControlGroup controlGroup) {
        this.controlGroup = controlGroup;
        return this;
    }

    /**
     * Gets the versionable value.
     *
     * @return the value, or <code>null</code> if undefined.
     */
    public Boolean versionable() {
        return versionable;
    }

    /**
     * Sets the versionable value.
     *
     * @param versionable the new value, possibly <code>null</code>.
     * @return this instance.
     */
    public Datastream versionable(Boolean versionable) {
        this.versionable = versionable;
        return this;
    }

    /**
     * Gets the (mutable) set of datastream versions for this
     * datastream. Iterators over the elements of the set will provide
     * the values in this order:
     * <ul>
     *   <li> First, any datastreams whose creation date is undefined
     *        will be provided in ascending order of their ids.</li>
     *   <li> Then, any datastreams whose creation date is defined will
     *        be provided in descending order of dates.  If multiple
     *        datastreams have the same creation date, they will occur in
     *        ascending order of their ids.</li>
     * </ul>
     *
     * @return the versions, possibly empty, but never <code>null</code>.
     */
    public SortedSet<DatastreamVersion> versions() {
        return versions;
    }

    /**
     * Creates and adds a new datastream version with an automatically
     * generated id that is unique within the existing versions.  The
     * generated id will start with <code>this.id() + "."</code> and have a
     * numeric suffix.
     *
     * @param createdDate the created date to use for the new datastream
     *        version, possibly <code>null</code>.
     * @return the new datastream version.
     */
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
