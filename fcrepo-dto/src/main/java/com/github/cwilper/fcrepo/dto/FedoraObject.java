package com.github.cwilper.fcrepo.dto;

import org.apache.http.client.HttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
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

    /**
     * Serializes the object to the given stream as FOXML, leaving the stream
     * open when finished.
     *
     * @param sink the stream to serialize to.
     * @param managedDatastreamsToEmbed the ids of any managed datastreams
     *        whose content should be base64-encoded within the FOXML rather
     *        than referenced.
     * @throws IOException if an error occurs while writing.
     */
    public void write(OutputStream sink, Set<String> managedDatastreamsToEmbed,
            HttpClient httpClient) throws IOException {
        new FOXMLWriter(this, sink, managedDatastreamsToEmbed, httpClient)
                .writeObject();
    }

    /**
     * Deserializes the FOXML from the given stream, closing the stream when
     * finished.
     *
     * @param source the stream to deserialize from.
     * @return a new FedoraObject instance populated from the given stream.
     * @throws IOException if an error occurs while reading.
     */
    public static FedoraObject read(InputStream source)
            throws IOException {
        return new FOXMLReader(source).readObject();
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
