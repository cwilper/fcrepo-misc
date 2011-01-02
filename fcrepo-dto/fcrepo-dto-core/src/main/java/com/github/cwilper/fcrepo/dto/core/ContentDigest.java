package com.github.cwilper.fcrepo.dto.core;

import java.util.Arrays;

/**
 * The computed checksum of a <code>DatastreamVersion</code>'s content.
 */
public class ContentDigest {

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
        this.type = type;
        return this;
    }

    public String hexValue() {
        return hexValue;
    }

    public ContentDigest hexValue(String hexValue) {
        this.hexValue = hexValue;
        return this;
    }

    @Override
    public final int hashCode() {
        return Util.computeHash(getEqArray());
    }

    @Override
    public final boolean equals(Object o) {
        return o instanceof ContentDigest && Arrays.equals(
                ((ContentDigest) o).getEqArray(), getEqArray());
    }

    Object[] getEqArray() {
        if (hexValue == null) {
            return new Object[] { type, null };
        } else {
            return new Object[] { type, hexValue.toUpperCase() };
        }
    }

}
