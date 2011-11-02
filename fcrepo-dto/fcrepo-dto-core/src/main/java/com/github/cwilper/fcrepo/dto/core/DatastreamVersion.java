package com.github.cwilper.fcrepo.dto.core;

import java.net.URI;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

import com.github.cwilper.fcrepo.dto.core.io.DateUtil;

/**
 * A particular revision of a {@link Datastream}.
 *
 * @see <a href="package-summary.html#working">Working With DTO Classes</a>
 */
public class DatastreamVersion extends FedoraDTO {

    private final SortedSet<URI> altIds;

    private final String id;
    private final Date createdDate;

    private String label;
    private String mimeType;
    private URI formatURI;
    private Long size;
    private ContentDigest contentDigest;
    private InlineXML inlineXML;
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
    	this.altIds=new TreeSet<URI>();
        this.id = Util.normalize(id);
        if (this.id == null) {
            throw new NullPointerException();
        }
        this.createdDate = Util.copy(createdDate);
    }

    /**
     * creates an instance from a DatastreamVersion.Builder
     * @param builder the builder holding the data
     */
    public DatastreamVersion(Builder builder) {
    	this.altIds=builder.altIds;
    	this.id=builder.id;
    	this.createdDate=builder.createdDate;
    	this.label=builder.label;
    	this.mimeType=builder.mimeType;
    	this.formatURI=builder.formatURI;
    	this.size=builder.size;
    	this.contentDigest=builder.contentDigest;
    	this.inlineXML=builder.inlineXML;
    	this.contentLocation=builder.contentLocation;
    }

	/**
     * Creates an instance based on the current state of this one.
     *
     * @return a deep copy.
     */
    public DatastreamVersion copy() {
        DatastreamVersion copy = new DatastreamVersion(id,
                                                       Util.copy(createdDate))
                .label(label)
                .mimeType(mimeType)
                .formatURI(formatURI)
                .size(size)
                .contentDigest(contentDigest == null ? null : contentDigest.copy())
                .inlineXML(inlineXML)
                .contentLocation(contentLocation);
        copy.altIds().addAll(altIds);
        return copy;
    }

    /**
     * Creates an instance based on the current state of this one, but with a
     * different id and createdDate.
     *
     * @param id the id of the version (not null, immutable).
     * @param createdDate the date the version was created
     *        (possibly null, immutable).
     * @return a deep copy.
     * @throws NullPointerException if id is given as <code>null</code>.
     */
    public DatastreamVersion copy(String id, Date createdDate) {
        DatastreamVersion copy = new DatastreamVersion(id,
                                                       Util.copy(createdDate))
                .label(label)
                .mimeType(mimeType)
                .formatURI(formatURI)
                .size(size)
                .contentDigest(contentDigest == null ? null : contentDigest.copy())
                .inlineXML(inlineXML)
                .contentLocation(contentLocation);
        copy.altIds().addAll(altIds);
        return copy;
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
     * Gets the size of the content, in bytes.
     *
     * @return the value, possibly <code>null</code>.
     */
    public Long size() {
        return size;
    }

    /**
     * Sets the size of the content, in bytes.
     *
     * @param size the new value, 0, positive, negative, or <code>null</code>.
     * @return this instance.
     */
    public DatastreamVersion size(Long size) {
        this.size = size;
        return this;
    }

    /**
     * Gets the inline XML content.
     *
     * @return the value, possibly <code>null</code>.
     */
    public InlineXML inlineXML() {
        return inlineXML;
    }

    /**
     * Sets the inline XML content.
     *
     * @param inlineXML the new value, possibly <code>null</code>.
     * @return this instance.
     */
    public DatastreamVersion inlineXML(InlineXML inlineXML) {
        this.inlineXML = inlineXML;
        return this;
    }

    /**
     * Gets the content location.
     *
     * @return the value, possibly <code>null</code>.
     */
    public URI contentLocation() {
        return contentLocation;
    }

    /**
     * Sets the content location.
     *
     * @param contentLocation the new value, possibly <code>null</code>.
     * @return this instance.
     */
    public DatastreamVersion contentLocation(URI contentLocation) {
        this.contentLocation = contentLocation;
        return this;
    }

    @Override
    Object[] getEqArray() {
        return new Object[] {
                id,
                label,
                DateUtil.toString(createdDate),
                mimeType,
                formatURI,
                contentDigest,
                size,
                inlineXML,
                contentLocation,
                altIds };
    }
    
    public static class Builder{

    	private final SortedSet<URI> altIds = new TreeSet<URI>();
        private final String id;
        private final Date createdDate;

        private String label;
        private String mimeType;
        private URI formatURI;
        private Long size;
        private ContentDigest contentDigest;
        private InlineXML inlineXML;
        private URI contentLocation;
        
		public Builder(String id, Date createdDate) {
			super();
        	if (id == null){
        		throw new NullPointerException("id can not be null");
        	}
			this.id = Util.normalize(id);
			this.createdDate = Util.copy(createdDate);
		}
        
        public Builder withLabel(String label){
        	this.label=label;
        	return this;
        }
        public Builder withMimeType(String mimeType){
        	this.mimeType=mimeType;
        	return this;
        }
        public Builder withFormatURI(URI formatURI){
        	this.formatURI=formatURI;
        	return this;
        }
        public Builder withSize(long size){
        	this.size=size;
        	return this;
        }
        public Builder withContentDigest(ContentDigest digest){
        	this.contentDigest=digest;
        	return this;
        }
        public Builder withInlineXML(InlineXML xml){
        	this.inlineXML=xml;
        	return this;
        }
        public Builder withContentLocation(URI location){
        	this.contentLocation=location;
        	return this;
        }
        public Builder withAlternateId(URI id){
        	this.altIds.add(id);
        	return this;
        }
        public DatastreamVersion build(){
        	return new DatastreamVersion(this);
        }
    }
}
