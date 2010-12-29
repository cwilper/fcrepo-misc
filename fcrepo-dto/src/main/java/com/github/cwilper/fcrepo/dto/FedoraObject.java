package com.github.cwilper.fcrepo.dto;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.*;

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

    /**
     * Serializes the object to the given writer as FOXML.
     *
     * @param sink the writer to serialize to. It will not be automatically
     *        closed.
     * @param managedDatastreamsToEmbed the ids of any managed datastreams
     *        whose content should be base64-encoded within the FOXML rather
     *        than referenced.
     */
    public void write(Writer sink,
                      Set<String> managedDatastreamsToEmbed) {
        new FOXMLWriter(sink, managedDatastreamsToEmbed).write();
    }

    /**
     * Serializes the object to the given stream as FOXML.
     *
     * @param sink the stream to serialize to. It will not be automatically
     *        closed.
     * @param managedDatastreamsToEmbed the ids of any managed datastreams
     *        whose content should be base64-encoded within the FOXML rather
     *        than referenced.
     */
    public void write(OutputStream sink,
                      Set<String> managedDatastreamsToEmbed) {
        new FOXMLWriter(sink, managedDatastreamsToEmbed).write();
    }

    /**
     * Deserializes the FOXML from the given reader.
     *
     * @param source the reader to deserialize from. It will not be
     *        automatically closed.
     * @return a new FedoraObject instance populated from the given stream.
     */
    public static FedoraObject read(Reader source) {
        return new FOXMLReader(source).read();
    }

    /**
     * Deserializes the FOXML from the given stream.
     *
     * @param source the stream to deserialize from. It will not be
     *        automatically closed.
     * @return a new FedoraObject instance populated from the given stream.
     */
    public static FedoraObject read(InputStream source) {
        return new FOXMLReader(source).read();
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
