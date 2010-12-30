package com.github.cwilper.fcrepo.dto.core;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

public class DatastreamVersion {

    private final SortedSet<URI> altIds = new TreeSet<URI>();

    private String id;
    private String label;
    private Date createdDate;
    private String mimeType;
    private URI formatURI;
    private Long size;
    private ContentDigest contentDigest;
    private byte[] xmlContent;
    private URI contentLocation;

    public DatastreamVersion() {
    }

    public String id() {
        return id;
    }

    public DatastreamVersion id(String id) {
        this.id = id;
        return this;
    }

    public String label() {
        return label;
    }

    public DatastreamVersion label(String label) {
        this.label = label;
        return this;
    }

    public Date createdDate() {
        return createdDate;
    }

    public DatastreamVersion createdDate(Date createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public String mimeType() {
        return mimeType;
    }

    public DatastreamVersion mimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    public SortedSet<URI> altIds() {
        return altIds;
    }

    public URI formatURI() {
        return formatURI;
    }

    public DatastreamVersion formatURI(URI formatURI) {
        this.formatURI = formatURI;
        return this;
    }

    public ContentDigest contentDigest() {
        return contentDigest;
    }

    public DatastreamVersion contentDigest(ContentDigest contentDigest) {
        this.contentDigest = contentDigest;
        return this;
    }

    public Long size() {
        return size;
    }

    public DatastreamVersion size(Long size) {
        this.size = size;
        return this;
    }

    public InputStream xmlContent() throws IOException {
        if (xmlContent == null) {
            return null;
        }
        return new ByteArrayInputStream(xmlContent);
    }

    // source will be read entirely, then auto-closed
    public void xmlContent(InputStream source) throws IOException {
        ByteArrayOutputStream sink = new ByteArrayOutputStream();
        try {
            IOUtils.copy(source, sink);
        } finally {
            IOUtils.closeQuietly(source);
            IOUtils.closeQuietly(sink);
        }
        xmlContent = sink.toByteArray();
    }

    public URI contentLocation() {
        return contentLocation;
    }

    public DatastreamVersion contentLocation(URI contentLocation) {
        this.contentLocation = contentLocation;
        return this;
    }

    @Override
    public final int hashCode() {
        return Util.computeHash(getEqArray());
    }

    @Override
    public final boolean equals(Object o) {
        return o instanceof DatastreamVersion && Arrays.equals(
                ((DatastreamVersion) o).getEqArray(), getEqArray());
    }
    
    Object[] getEqArray() {
        return new Object[] { id, label, createdDate, mimeType, formatURI,
                contentDigest, size, xmlContent, contentLocation, altIds };
    }

}
