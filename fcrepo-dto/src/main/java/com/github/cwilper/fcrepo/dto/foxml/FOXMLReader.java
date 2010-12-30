package com.github.cwilper.fcrepo.dto.foxml;

import com.github.cwilper.fcrepo.dto.core.FedoraObject;

import java.io.IOException;
import java.io.InputStream;

public class FOXMLReader {

    private InputStream source;

    public FOXMLReader() {
    }

    public FedoraObject read(InputStream source) throws IOException {
        this.source = source;
        // TODO: Implement this
        throw new UnsupportedOperationException();
    }
}
