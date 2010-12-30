package com.github.cwilper.fcrepo.dto.core;

import java.util.Arrays;

public class ContentDigest {

    private String type;
    private String hexValue;

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
        return new Object[] { type, hexValue.toUpperCase() };
    }

}
