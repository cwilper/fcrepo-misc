package com.github.cwilper.fcrepo.dto;

import java.io.IOException;
import java.io.InputStream;

class FOXMLReader {

    private final InputStream source;

    public FOXMLReader(InputStream source) {
        this.source = source;
    }

    public FedoraObject readObject() throws IOException {
        // TODO: Implement this
        throw new UnsupportedOperationException();
    }
}
