package com.github.cwilper.fcrepo.dto.core;

import org.apache.commons.io.IOUtils;

import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A particular revision of a <code>Datastream</code>.
 */
public class DatastreamVersion extends AbstractDTO {

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
        this.id = Util.normalize(id);
        if (this.id == null) {
            throw new NullPointerException();
        }
        this.createdDate = Util.copy(createdDate);
    }

    public String id() {
        return id;
    }

    public String label() {
        return label;
    }

    public DatastreamVersion label(String label) {
        this.label = Util.normalize(label);
        return this;
    }

    public Date createdDate() {
        return Util.copy(createdDate);
    }

    public String mimeType() {
        return mimeType;
    }

    public DatastreamVersion mimeType(String mimeType) {
        this.mimeType = Util.normalize(mimeType);
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
        if (size != null && size < 0) throw new IllegalArgumentException();
        this.size = size;
        return this;
    }

    public boolean hasInlineXML() {
        return inlineXML != null;
    }

    public DatastreamVersion getInlineXML(Writer sink) throws IOException {
        if (inlineXML != null) {
            IOUtils.copy(new CharArrayReader(inlineXML), sink);
        }
        return this;
    }

    // source will be read entirely, then auto-closed
    // TODO: normalize if needed (omit xml decl, omit doctype, strip leading/trailing spaces)
    // and use axiom to canonicalize the xml
    // then...auto-set size (probably not, or have an OPTION)??
    public DatastreamVersion setInlineXML(Reader source) throws IOException {
        if (source == null) {
            inlineXML = null;
        } else {
            CharArrayWriter sink = new CharArrayWriter();
            try {
                IOUtils.copy(source, sink);
            } finally {
                IOUtils.closeQuietly(source);
            }
            inlineXML = sink.toCharArray();
        }
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
    Object[] getEqArray() {
        return new Object[] { id, label, createdDate, mimeType, formatURI,
                contentDigest, size, inlineXML, contentLocation, altIds };
    }

}
