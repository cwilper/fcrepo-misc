package com.github.cwilper.fcrepo.dto.core.io;

import com.github.cwilper.fcrepo.dto.core.Datastream;
import com.github.cwilper.fcrepo.dto.core.DatastreamVersion;
import com.github.cwilper.fcrepo.dto.core.FedoraObject;

import java.io.IOException;
import java.io.InputStream;

public interface ContentHandler {

    void handleContent(FedoraObject obj,
                       Datastream ds,
                       DatastreamVersion dsv,
                       InputStream content) throws IOException;

    void close();
}
