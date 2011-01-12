package com.github.cwilper.fcrepo.dto.core;

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
    public void typeField() {
        checkStringField(new ContentDigest(), "type");
    }

    @Test
    public void hexValueField() {
        checkStringField(new ContentDigest(), "hexValue");
    }

}
