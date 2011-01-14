package com.github.cwilper.fcrepo.dto.core;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for <code>ContentDigest</code>.
 */
public class ContentDigestTest extends FedoraDTOTest {

    @Override
    Object[] getEqualInstances() {
        return new Object[] {
                new ContentDigest(),
                new ContentDigest(),
                new ContentDigest().type("a"),
                new ContentDigest().type("a"),
                new ContentDigest().hexValue("a").type("b"),
                new ContentDigest().hexValue("a").type("b")
        };
    }

    @Override
    Object[] getNonEqualInstances() {
        return new Object[] {
                new ContentDigest(),
                new ContentDigest().type("a")
        };
    }

    @Test
    public void copy() {
        ContentDigest o1 = new ContentDigest().type("a").hexValue("a");
        ContentDigest o2 = o1.copy();
        Assert.assertEquals(o1, o2);
        Assert.assertNotSame(o1, o2);
        o1.type("b");
        Assert.assertFalse(o1.equals(o2));
    }

    @Test
    public void typeField() {
        checkStringField(new ContentDigest(), "type");
    }

    @Test
    public void hexValueField() {
        checkStringField(new ContentDigest(), "hexValue");
    }

}
