package com.github.cwilper.fcrepo.dto.core;

/**
 * The computed checksum of a <code>DatastreamVersion</code>'s content.
 *
 * @see <a href="package-summary.html#working">Working With DTO Classes</a>
 */
public class ContentDigest extends FedoraDTO {

    private String type;
    private String hexValue;

    /**
     * Creates an instance.
     */
    public ContentDigest() {
    }

    /**
     * Gets the type.
     *
     * @return the value, or <code>null</code> if undefined.
     */
    public String type() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type the new value, which will be string-normalized.
     * @return this instance.
     */
    public ContentDigest type(String type) {
        this.type = Util.normalize(type);
        return this;
    }

    /**
     * Gets the hex value.
     *
     * @return the value, or <code>null</code> if undefined.
     */
    public String hexValue() {
        return hexValue;
    }

    /**
     * Sets the hex value.
     *
     * @param hexValue the new value, which will be string-normalized.
     * @return this instance.
     */
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
