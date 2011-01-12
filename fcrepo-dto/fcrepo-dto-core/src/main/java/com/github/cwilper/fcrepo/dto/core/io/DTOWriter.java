package com.github.cwilper.fcrepo.dto.core.io;

import com.github.cwilper.fcrepo.dto.core.FedoraObject;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Interface for writing a <code>FedoraObject</code> to a stream.
 */
public interface DTOWriter {

    /**
     * Serializes a <code>FedoraObject</code> to the given stream.
     *
     * @param obj the <code>FedoraObject</code> to serialize.
     * @param sink the stream to write to. It will not be automatically
     *        closed by this method, regardless of success.
     * @throws IOException if the stream cannot be serialized for any reason.
     */
    void writeObject(FedoraObject obj, OutputStream sink) throws IOException;

    /**
     * Releases all resources associated with this writer.
     * This can be safely called multiple times.
     */
    void close();
}
