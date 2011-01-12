package com.github.cwilper.fcrepo.dto.core;

/**
 * The computed checksum of a <code>DatastreamVersion</code>'s content.
 * <p>
 * A content digest has a type (<code>MD5</code>, <code>SHA-1</code>, etc.)
 * and a hex value (e.g. <code>ea309a2eb146a4d8bdf8946c4df23c70</code>).
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
     * Gets the type of digest.
     *
     * @return the value, or <code>null</code> if undefined.
     */
    public String type() {
        return type;
    }

    /**
     * Sets the type of digest.
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
