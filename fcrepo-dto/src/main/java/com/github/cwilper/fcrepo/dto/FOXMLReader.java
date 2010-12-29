package com.github.cwilper.fcrepo.dto;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

class FOXMLReader {

    private final Reader source;

    public FOXMLReader(InputStream source) {
        this(new InputStreamReader(source, Charset.forName("UTF-8")));
    }

    public FOXMLReader(Reader source) {
        this.source = source;
    }

    public FedoraObject read() {
        // TODO: Implement this
        throw new UnsupportedOperationException();
    }
}
