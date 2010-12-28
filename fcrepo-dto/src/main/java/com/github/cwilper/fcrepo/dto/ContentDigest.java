package com.github.cwilper.fcrepo.dto;

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

}
