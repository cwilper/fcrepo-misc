package com.github.cwilper.fcrepo.dto.core;

import org.junit.Assert;

/**
 * Superclass providing convenience methods for tests in this package.
 */
abstract class AbstractTest {

    static void checkEquality(Object a, Object b) {
        Assert.assertEquals(a, b);
        Assert.assertEquals(a.hashCode(), b.hashCode());
    }
}
