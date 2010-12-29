package com.github.cwilper.fcrepo.dto;

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

    private final SortedSet<String> altIds = new TreeSet<String>();

    private String id;
    private String label;
    private Date createdDate;
    private String mimeType;
    private URI formatURI;
    private ContentDigest contentDigest;
    private Long size;
    private byte[] inlineXMLContent;
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

    public SortedSet<String> altIds() {
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

    public InputStream getInlineXMLContent() {
        if (inlineXMLContent == null) {
            return null;
        }
        return new ByteArrayInputStream(inlineXMLContent);
    }

    public void setInlineXMLContent(InputStream source) {
        ByteArrayOutputStream sink = new ByteArrayOutputStream();
        try {
            byte[] buf = new byte[4096];
            int len;
            while ((len = source.read(buf)) > 0) {
                sink.write(buf, 0, len);
            }
        } catch (IOException e) {
            throw new RuntimeException(
                    "Error setting XML content from stream", e);
        } finally {
            try {
                source.close();
                sink.close();
            } catch (IOException e) {
            }
        }
        inlineXMLContent = sink.toByteArray();
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
                contentDigest, size, inlineXMLContent, contentLocation,
                altIds };
    }

}
