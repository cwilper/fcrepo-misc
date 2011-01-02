package com.github.cwilper.fcrepo.dto.core;

import org.apache.commons.io.IOUtils;

import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.util.Arrays;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A particular revision of a <code>Datastream</code>.
 */
public class DatastreamVersion {

    private final SortedSet<URI> altIds = new TreeSet<URI>();

    private final String id;
    private final Date createdDate;

    private String label;
    private String mimeType;
    private URI formatURI;
    private Long size;
    private ContentDigest contentDigest;
    private char[] inlineXML;
    private URI contentLocation;

    /**
     * Creates an instance.
     *
     * @param id the id of the version (not null, immutable).
     * @param createdDate the date the version was created
     *        (possibly null, immutable).
     * @throws NullPointerException if id is given as <code>null</code>.
     */
    public DatastreamVersion(String id, Date createdDate) {
        if (id == null) {
            throw new NullPointerException();
        }
        this.id = id;
        if (createdDate == null) {
            this.createdDate = null;
        } else {
            this.createdDate = new Date(createdDate.getTime());
        }
    }

    public String id() {
        return id;
    }

    public String label() {
        return label;
    }

    public DatastreamVersion label(String label) {
        this.label = label;
        return this;
    }

    public Date createdDate() {
        if (createdDate == null) {
            return null;
        } else {
            return new Date(createdDate.getTime());
        }
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

    public boolean inlineXML() {
        return inlineXML != null;
    }

    public DatastreamVersion inlineXML(Writer writer) throws IOException {
        if (inlineXML != null) {
            IOUtils.copy(new CharArrayReader(inlineXML), writer);
        }
        return this;
    }

    // source will be read entirely, then auto-closed
    public DatastreamVersion inlineXML(Reader reader) throws IOException {
        CharArrayWriter writer = new CharArrayWriter();
        try {
            IOUtils.copy(reader, writer);
        } finally {
            IOUtils.closeQuietly(reader);
        }
        inlineXML = writer.toCharArray();
        return this;
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
                contentDigest, size, inlineXML, contentLocation, altIds };
    }

}
