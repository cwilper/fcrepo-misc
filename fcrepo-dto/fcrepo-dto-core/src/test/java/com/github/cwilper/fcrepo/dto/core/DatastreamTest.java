package com.github.cwilper.fcrepo.dto.core;

import org.junit.Test;

public class DatastreamTest extends AbstractTest {

    @Test (expected=NullPointerException.class)
    public void nullId() {
        new Datastream(null);
    }

    @Test
    public void equality() {
        checkEquality(new Datastream("a"), new Datastream("a"));
    }

}
