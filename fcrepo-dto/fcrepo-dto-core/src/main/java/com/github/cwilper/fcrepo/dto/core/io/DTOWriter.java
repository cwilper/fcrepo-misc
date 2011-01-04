package com.github.cwilper.fcrepo.dto.core.io;

import com.github.cwilper.fcrepo.dto.core.FedoraObject;

import java.io.IOException;
import java.io.OutputStream;

public interface DTOWriter {

    void writeObject(FedoraObject obj, OutputStream sink) throws IOException;

    void close();
}
