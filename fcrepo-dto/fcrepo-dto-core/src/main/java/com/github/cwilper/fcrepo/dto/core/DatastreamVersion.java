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
 *
 * @see <a href="package-summary.html#working">Working With DTO Classes</a>
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
    private String inlineXML;
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

    /**
     * Gets the id.
     *
     * @return the value, never <code>null</code>.
     */
    public String id() {
        return id;
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
    public DatastreamVersion label(String label) {
        this.label = Util.normalize(label);
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
     * Gets the mime type of the content.
     *
     * @return the value, possibly <code>null</code>.
     */
    public String mimeType() {
        return mimeType;
    }

    /**
     * Sets the mime type of the content.
     *
     * @param mimeType the new value, which will be string-normalized.
     * @return this instance.
     */
    public DatastreamVersion mimeType(String mimeType) {
        this.mimeType = Util.normalize(mimeType);
        return this;
    }

    /**
     * Gets the (mutable) set of alternate ids. Iterators over the elements
     * of the set will provide the values in ascending alphabetical order.
     *
     * @return the set, possibly empty, never <code>null</code>.
     */
    public SortedSet<URI> altIds() {
        return altIds;
    }

    /**
     * Gets the format URI.
     *
     * @return the value, possibly <code>null</code>.
     */
    public URI formatURI() {
        return formatURI;
    }

    /**
     * Sets the format URI.
     *
     * @param formatURI the new value, possibly <code>null</code>.
     * @return this instance.
     */
    public DatastreamVersion formatURI(URI formatURI) {
        this.formatURI = formatURI;
        return this;
    }

    /**
     * Gets the content digest.
     *
     * @return the value, possibly <code>null</code>.
     */
    public ContentDigest contentDigest() {
        return contentDigest;
    }

    /**
     * Sets the content digest.
     *
     * @param contentDigest the new value, possibly <code>null</code>.
     * @return this instance.
     */
    public DatastreamVersion contentDigest(ContentDigest contentDigest) {
        this.contentDigest = contentDigest;
        return this;
    }

    /**
     * Gets the size of the content.
     *
     * @return the value, possibly <code>null</code>.
     */
    public Long size() {
        return size;
    }

    /**
     * Sets the size of the content.
     *
     * @param size the new value, 0, positive, negative, or <code>null</code>.
     * @return this instance.
     */
    public DatastreamVersion size(Long size) {
        this.size = size;
        return this;
    }

    public byte[] inlineXMLBytes() {
        return Util.getBytes(inlineXML);
    }

    public String inlineXML() {
        return inlineXML;
    }

    public DatastreamVersion inlineXML(String inlineXML) throws IOException {
        return inlineXMLBytes(Util.getBytes(inlineXML));
    }

    public DatastreamVersion inlineXMLBytes(byte[] inlineXMLBytes)
            throws IOException {
        if (inlineXMLBytes != null) {
            try {
                inlineXML = Util.getString(
                        XMLUtil.canonicalize(inlineXMLBytes));
                inlineXMLCanonicalized = true;
            } catch (Exception e) {
                logger.debug("Can't canonicalize inlineXML; pretty-printing "
                        + "instead", e);
                inlineXML = Util.getString(
                        XMLUtil.prettyPrint(inlineXMLBytes, true));
                inlineXMLCanonicalized = false;
            }
        } else {
            inlineXML = null;
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
