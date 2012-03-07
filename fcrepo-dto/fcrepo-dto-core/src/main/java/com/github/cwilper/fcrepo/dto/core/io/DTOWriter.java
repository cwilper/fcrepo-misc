package com.github.cwilper.fcrepo.dto.core.io;

import com.github.cwilper.fcrepo.dto.core.FedoraObject;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Interface for writing a {@link FedoraObject} to a stream.
 */
public interface DTOWriter {
    /**
     * Gets a new instance configured like this one.
     *
     * @return the instance.
     */
    DTOWriter getInstance();

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
