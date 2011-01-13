package com.github.cwilper.fcrepo.dto.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

/**
 * Callback interface used to resolve referenced content as needed while
 * writing a {@link com.github.cwilper.fcrepo.dto.core.FedoraObject} to a
 * stream.
 */
public interface ContentResolver {

    /**
     * Gets a stream for reading the content at the given location.
     *
     * @param base the base URI that may be used if <code>ref</code> is
     *        relative and the resolver implementation only works with
     *        absolute URIs, possibly <code>null</code>.
     * @param ref the absolute or relative URI of the content to resolve,
     *        never <code>null</code>.
     * @return an input stream positioned at the content. This must be closed
     *         by the caller when finished.
     * @throws IllegalArgumentException if base is specified but not absolute.
     * @throws NullPointerException if ref is given as null.
     * @throws IOException if the content was not found or any other error
     *         occurred.
     */
    InputStream resolveContent(URI base, URI ref) throws IOException;

    /**
     * Writes the content from the given location to the given stream.
     *
     * @param base the base URI that may be used if <code>ref</code> is
     *        relative and the resolver implementation only works with
     *        absolute URIs, possibly <code>null</code>.
     * @param ref the absolute or relative URI of the content to resolve,
     *        never <code>null</code>.
     * @throws IllegalArgumentException if base is specified but not absolute.
     * @throws NullPointerException if ref is given as null.
     * @throws IOException if the content was not found or any other error
     *         occurred.
     */
    void resolveContent(URI base, URI ref, OutputStream sink) throws IOException;

    /**
     * Releases all resources associated with this resolver.
     * This can safely be called multiple times.
     */
    void close();

}
