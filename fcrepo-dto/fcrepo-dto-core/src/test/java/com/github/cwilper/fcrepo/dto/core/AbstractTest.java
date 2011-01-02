package com.github.cwilper.fcrepo.dto.core;

import org.junit.Assert;

abstract class AbstractTest {

    public void checkEquality(Object a, Object b) {
        Assert.assertEquals(a, b);
        Assert.assertEquals(a.hashCode(), b.hashCode());
    }
}
