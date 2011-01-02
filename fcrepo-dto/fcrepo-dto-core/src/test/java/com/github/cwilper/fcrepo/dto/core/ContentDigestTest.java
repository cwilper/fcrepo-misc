package com.github.cwilper.fcrepo.dto.core;

import org.junit.Assert;
import org.junit.Test;

public class ContentDigestTest extends AbstractTest {

    @Test
    public void type() {
        ContentDigest contentDigest = new ContentDigest().type("a");
        Assert.assertEquals("a", contentDigest.type());
    }

    @Test
    public void hexValue() {
        ContentDigest contentDigest = new ContentDigest().hexValue("a");
        Assert.assertEquals("a", contentDigest.hexValue());
    }

    @Test
    public void equality() {
        checkEquality(new ContentDigest(), new ContentDigest());
        checkEquality(new ContentDigest().type("a"),
                new ContentDigest().type("a"));
        checkEquality(new ContentDigest().hexValue("a"),
                new ContentDigest().hexValue("a"));
        checkEquality(new ContentDigest().type("a").hexValue("a"),
                new ContentDigest().type("a").hexValue("a"));
    }

    @Test
    public void inequality() {
        Assert.assertFalse(new ContentDigest().equals(""));
        Assert.assertFalse(new ContentDigest().equals(
                new ContentDigest().type("a")));
        Assert.assertFalse(new ContentDigest().equals(
                new ContentDigest().hexValue("a")));
        Assert.assertFalse(new ContentDigest().type("a").equals(
                new ContentDigest().hexValue("a")));
    }

}
