package com.github.cwilper.fcrepo.dto.core.io;

import com.github.cwilper.fcrepo.dto.core.Datastream;
import com.github.cwilper.fcrepo.dto.core.DatastreamVersion;
import com.github.cwilper.fcrepo.dto.core.FedoraObject;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Callback interface used to handle content if needed when inline managed
 * content is encountered while reading a {@link FedoraObject} from a stream.
 */
public interface ContentHandler {

    /**
     * Handles the content of the given <code>DatastreamVersion</code>.
     * <p>
     * An implementation MAY:
     * <ul>
     *   <li> Modify the fields of the given <code>FedoraObject</code>,
     *        <code>Datastream</code>, or <code>DatastreamVersion</code>.
     *   <li> Opt out of having the content stored, in which case it should
     *        return <code>null</code>.
     * </ul>
     * <p>
     * A <i>typical</i> implementation of this method will allocate a file
     * to which the content may be written, set the datastream version's
     * content location to the URI of that file, and return an
     * <code>OutputStream</code> so the caller may write the content.
     *
     * @param obj the object containing the datastream.
     * @param ds the datastream containing the datastream version.
     * @param dsv the datastream version whose content is to be handled.
     * @return the sink to which the caller should write the content, or
     *         <code>null</code> if the caller should not write the content.
     *         If non-null, the caller is expected to close the stream when
     *         finished writing the content to it.
     * @throws IOException if the content cannot be handled for any reason.
     */
    OutputStream handleContent(FedoraObject obj,
                               Datastream ds,
                               DatastreamVersion dsv) throws IOException;

    /**
     * Releases all resources associated with this handler.
     * This can be safely called multiple times.
     */
    void close();
}
