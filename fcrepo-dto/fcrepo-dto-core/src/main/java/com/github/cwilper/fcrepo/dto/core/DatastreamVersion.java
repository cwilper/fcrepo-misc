package com.github.cwilper.fcrepo.dto.core;

import com.github.cwilper.fcrepo.dto.core.io.XMLUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A particular revision of a <code>Datastream</code>.
 */
public class DatastreamVersion extends FedoraDTO {

    private final static Logger logger =
            LoggerFactory.getLogger(DatastreamVersion.class);

    private final SortedSet<URI> altIds = new TreeSet<URI>();

    private final String id;
    private final Date createdDate;

    private String label;
    private String mimeType;
    private URI formatURI;
    private Long size;
    private ContentDigest contentDigest;
    private byte[] inlineXML;
    private URI contentLocation;

    private boolean inlineXMLCanonicalized;

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

    public byte[] inlineXML() {
        return inlineXML;
    }

    public DatastreamVersion inlineXML(byte[] inlineXML) throws IOException {
        if (inlineXML != null) {
            try {
                this.inlineXML = XMLUtil.canonicalize(inlineXML);
                inlineXMLCanonicalized = true;
            } catch (Exception e) {
                logger.debug("Can't canonicalize inlineXML; pretty-printing "
                        + "instead", e);
                this.inlineXML = XMLUtil.prettyPrint(inlineXML, true);
                inlineXMLCanonicalized = false;
            }
        } else {
            this.inlineXML = null;
            inlineXMLCanonicalized = false;
        }
        return this;
    }

    public boolean inlineXMLCanonicalized() {
        return inlineXMLCanonicalized;
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
