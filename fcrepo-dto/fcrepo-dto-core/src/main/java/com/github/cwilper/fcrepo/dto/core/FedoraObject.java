package com.github.cwilper.fcrepo.dto.core;

import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A Fedora Digital Object.
 *
 * @see <a href="package-summary.html#working">Working With DTO Classes</a>
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

    /**
     * Gets the pid.
     *
     * @return the value, possibly <code>null</code>.
     */
    public String pid() {
        return pid;
    }

    /**
     * Sets the pid.
     *
     * @param pid the new value, which will be string-normalized.
     * @return this instance.
     */
    public FedoraObject pid(String pid) {
        this.pid = Util.normalize(pid);
        return this;
    }

    /**
     * Gets the state.
     *
     * @return the value, possibly <code>null</code>.
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
    public FedoraObject state(State state) {
        this.state = state;
        return this;
    }

    /**
     * Gets the label.
     *
     * @return the value, possibly <code>null</code>.
     */
    public String label() {
        return label;
    }

    /**
     * Sets the label.
     *
     * @param label the new value, which will be string-normalized.
     * @return this instance.
     */
    public FedoraObject label(String label) {
        this.label = Util.normalize(label);
        return this;
    }

    /**
     * Gets the owner id.
     *
     * @return the value, possibly <code>null</code>.
     */
    public String ownerId() {
        return ownerId;
    }

    /**
     * Sets the owner id.
     *
     * @param ownerId the new value, which will be string-normalized.
     * @return this instance.
     */
    public FedoraObject ownerId(String ownerId) {
        this.ownerId = Util.normalize(ownerId);
        return this;
    }

    /**
     * Gets the created date.
     *
     * @return the value, possibly <code>null</code>.
     */
    public Date createdDate() {
        return Util.copy(createdDate);
    }

    /**
     * Sets the created date.
     *
     * @param createdDate the value, possibly <code>null</code>.
     * @return this instance.
     */
    public FedoraObject createdDate(Date createdDate) {
        this.createdDate = Util.copy(createdDate);
        return this;
    }

    /**
     * Gets the last modified date.
     *
     * @return the value, possibly <code>null</code>.
     */
    public Date lastModifiedDate() {
        return Util.copy(lastModifiedDate);
    }

    /**
     * Sets the last modified date.
     *
     * @param lastModifiedDate the value, possibly <code>null</code>.
     * @return this instance.
     */
    public FedoraObject lastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = Util.copy(lastModifiedDate);
        return this;
    }

    /**
     * Adds or replaces a datastream in this object. If a datastream already
     * exists in the object with a matching id, it will be replaced by this
     * one. This is a convenience method which is equivalent to:
     * <pre>
     *     obj.datastreams().put(ds.id(), ds);
     * </pre>
     *
     * @param ds the value to add, never <code>null</code>.
     * @return this instance.
     * @throws NullPointerException if the value is null.
     */
    public FedoraObject putDatastream(Datastream ds) {
        datastreams.put(ds.id(), ds);
        return this;
    }

    /**
     * Gets the (mutable) map of datastreams in this object. Iterators over
     * the keys of the map will provide the values in alphabetical order
     * (descending) according to their ids.
     * <p>
     * The returned map ensures that when datastreams are added via
     * <code>put(key, value)</code>, the key matches the id of the datastream.
     * If this is violated, an {@link IllegalArgumentException} is
     * thrown.
     * <p>
     * Any attempt to put a <code>null</code> key or value will result in a
     * {@link NullPointerException}.
     *
     * @return the value, possibly empty, never <code>null</code>.
     */
    public SortedMap<String, Datastream> datastreams() {
        return datastreams;
    }

    @Override
    Object[] getEqArray() {
        return new Object[] { pid, state, label, ownerId, createdDate,
                lastModifiedDate, datastreams };
    }

    // ensures datastreams can't be added with a key differing from their id
    private static class DSMap extends TreeMap<String, Datastream> {

        @Override
        public Datastream put(String id, Datastream datastream) {
            if (id == null) throw new NullPointerException();
            if (!datastream.id().equals(id)) {
                throw new IllegalArgumentException();
            }
            return super.put(id, datastream);
        }
        
    }

}
