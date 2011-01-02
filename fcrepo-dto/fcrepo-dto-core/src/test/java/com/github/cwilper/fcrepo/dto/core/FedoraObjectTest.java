package com.github.cwilper.fcrepo.dto.core;

import org.junit.Test;

/**
 * Unit tests for <code>FedoraObject</code>.
 */
public class FedoraObjectTest extends AbstractTest {

    @Test
    public void equality() {
        checkEquality(new FedoraObject(), new FedoraObject());
    }
}
