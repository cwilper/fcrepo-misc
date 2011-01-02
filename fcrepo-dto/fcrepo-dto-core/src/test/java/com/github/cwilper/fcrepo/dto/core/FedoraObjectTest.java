package com.github.cwilper.fcrepo.dto.core;

import org.junit.Test;

public class FedoraObjectTest extends AbstractTest {

    @Test
    public void equality() {
        checkEquality(new FedoraObject(), new FedoraObject());
    }
}
