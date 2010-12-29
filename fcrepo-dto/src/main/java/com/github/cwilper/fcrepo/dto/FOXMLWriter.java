package com.github.cwilper.fcrepo.dto;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Set;

class FOXMLWriter {

    private final Writer sink;

    public FOXMLWriter(OutputStream sink,
                       Set<String> managedDatastreamsToEmbed) {
        this(new OutputStreamWriter(sink, Charset.forName("UTF-8")),
                managedDatastreamsToEmbed);
    }

    public FOXMLWriter(Writer sink,
                       Set<String> managedDatastreamsToEmbed) {
        this.sink = sink;
    }

    public void write() {
        // TODO: Implement this
        throw new UnsupportedOperationException();
    }
}
