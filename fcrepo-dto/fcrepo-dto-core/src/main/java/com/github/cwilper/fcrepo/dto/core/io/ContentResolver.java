package com.github.cwilper.fcrepo.dto.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

public interface ContentResolver {

    InputStream resolveContent(URI base, URI ref) throws IOException;

    void resolveContent(URI base, URI ref, OutputStream sink) throws IOException;

    void close();

}
