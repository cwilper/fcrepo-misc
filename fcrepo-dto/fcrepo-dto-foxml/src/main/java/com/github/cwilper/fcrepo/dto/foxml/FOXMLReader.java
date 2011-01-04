package com.github.cwilper.fcrepo.dto.foxml;

import com.github.cwilper.fcrepo.dto.core.FedoraObject;
import com.github.cwilper.fcrepo.dto.core.io.AbstractDTOReader;

import java.io.IOException;
import java.io.InputStream;

public class FOXMLReader extends AbstractDTOReader {

    private InputStream source;

    public FOXMLReader() {
    }

    public FedoraObject readObject(InputStream source) throws IOException {
        this.source = source;
        // TODO: Implement this
        throw new UnsupportedOperationException();
    }
}
