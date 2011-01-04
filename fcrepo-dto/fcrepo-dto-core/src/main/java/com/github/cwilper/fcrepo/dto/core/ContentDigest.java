package com.github.cwilper.fcrepo.dto.core;

/**
 * The computed checksum of a <code>DatastreamVersion</code>'s content.
 */
public class ContentDigest extends AbstractDTO {

    private String type;
    private String hexValue;

    /**
     * Creates an instance.
     */
    public ContentDigest() {
    }

    public String type() {
        return type;
    }

    public ContentDigest type(String type) {
        this.type = Util.normalize(type);
        return this;
    }

    public String hexValue() {
        return hexValue;
    }

    public ContentDigest hexValue(String hexValue) {
        this.hexValue = Util.normalize(hexValue);
        return this;
    }

    @Override
    Object[] getEqArray() {
        if (hexValue == null) {
            return new Object[] { type, null };
        } else {
            return new Object[] { type, hexValue.toUpperCase() };
        }
    }

}
