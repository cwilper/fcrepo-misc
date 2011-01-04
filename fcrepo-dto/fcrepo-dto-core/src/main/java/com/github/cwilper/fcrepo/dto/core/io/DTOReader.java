package com.github.cwilper.fcrepo.dto.core.io;

import com.github.cwilper.fcrepo.dto.core.FedoraObject;

import java.io.IOException;
import java.io.InputStream;

public interface DTOReader {

    FedoraObject readObject(InputStream source) throws IOException;

    void close();
    
}
