package com.github.cwilper.fcrepo.dto.core.io;

import com.github.cwilper.fcrepo.dto.core.Datastream;
import com.github.cwilper.fcrepo.dto.core.DatastreamVersion;
import com.github.cwilper.fcrepo.dto.core.FedoraObject;

import java.io.IOException;
import java.io.OutputStream;

public interface ContentHandler {

    OutputStream handleContent(FedoraObject obj,
                               Datastream ds,
                               DatastreamVersion dsv) throws IOException;

    void close();
}
