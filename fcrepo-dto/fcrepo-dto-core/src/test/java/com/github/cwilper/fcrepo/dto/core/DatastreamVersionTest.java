package com.github.cwilper.fcrepo.dto.core;

import org.junit.Test;

public class DatastreamVersionTest extends AbstractTest {

    @Test (expected=NullPointerException.class)
    public void nullId() {
        new DatastreamVersion(null, null);
    }

    @Test
    public void equality() {
        checkEquality(new DatastreamVersion("a", null),
                new DatastreamVersion("a", null));
    }
}
