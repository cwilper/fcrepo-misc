package com.github.cwilper.fcrepo.dto.core.io;

import com.github.cwilper.fcrepo.dto.core.Datastream;
import com.github.cwilper.fcrepo.dto.core.DatastreamVersion;
import com.github.cwilper.fcrepo.dto.core.FedoraObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A {@link ContentHandler} that allocates files in a single local directory,
 * using the filename <code>pid + dsId + dsVersionId</code> and sets the
 * datastream <code>contentLocation</code> to a the corresponding
 * <code>file:///</code> URI.
 * <p>
 * If the pid is <code>null</code>, that portion of the filename will be
 * a unique string starting with <code>nopid:</code> and ending with a
 * numeric value.
 * <p>
 * Initially, the directory to write to will be a new, temporary directory
 * as provided by {@link File#createTempFile(String, String)}. The directory
 * to use can be overridden via {@link #setBaseDir(File)} before the handler
 * is used.
 * <p>
 * Content that is written will be automatically deleted when the handler
 * is {@link #close()}d, unless {@link #setAutoDelete(boolean)} is called
 * beforehand.
 */
public class DefaultContentHandler implements ContentHandler {

    private static Logger logger = LoggerFactory.getLogger(
            DefaultContentHandler.class);

    private File baseDir;
    private boolean autoDelete;

    /**
     * Creates an instance.
     */
    public DefaultContentHandler() {
        autoDelete = true;
    }

    /**
     * Sets the base directory. If the new base directory is different from
     * the current one and <code>autoDelete</code> is true, the current
     * base directory's will be deleted. The new base directory need not exist
     * yet. If it doesn't exist, it and all parent directories will be created
     * the first time it's needed.
     *
     * @param baseDir the new value, never <code>null</code>.
     * @throws NullPointerException if the value is null.
     */
    public void setBaseDir(File baseDir) {
        if (this.baseDir != null && this.baseDir != baseDir && autoDelete) {
            rmDir(this.baseDir);
        }
        this.baseDir = baseDir;
    }

    /**
     * Sets whether the base directory's contents should be deleted when
     * this handler is closed or a new base directory is set.
     *
     * @param autoDelete the new value.
     */
    public void setAutoDelete(boolean autoDelete) {
        this.autoDelete = autoDelete;
    }

    @Override
    public OutputStream handleContent(FedoraObject obj,
                                      Datastream ds,
                                      DatastreamVersion dsv)
            throws IOException {
        File file = new File(baseDir(), getPath(obj, ds, dsv));
        if (!file.getParentFile().mkdirs()) {
            throw new IOException("Can't create parent dir of " + file);
        }
        OutputStream sink = new FileOutputStream(file);
        dsv.contentLocation(file.toURI());
        return sink;
    }

    private static String getPath(FedoraObject obj,
                                  Datastream ds,
                                  DatastreamVersion dsv) {
        if (obj == null || ds == null || dsv == null) {
            throw new NullPointerException();
        }
        StringBuffer sb = new StringBuffer();
        if (obj.pid() != null) {
            sb.append(obj.pid());
        } else {
            // if pid isn't assigned yet, come up with a unique enough path
            // to avoid collisions for now
            sb.append("nopid:" + Math.abs((long) obj.hashCode()));
        }
        sb.append("/");
        sb.append(ds.id());
        sb.append("+");
        sb.append(dsv.id());
        return sb.toString();
    }

    // allocates the temporary base directory lazily
    private File baseDir() throws IOException {
        if (baseDir == null) {
            baseDir = File.createTempFile("fcrepo-dto", null);
            if (!baseDir.delete()) {
                throw new IOException("Can't delete temp file " + baseDir);
            }
            if (!baseDir.mkdir()) {
                throw new IOException("Can't create temp dir " + baseDir);
            }
        }
        return baseDir;
    }

    @Override
    @PreDestroy
    public void close() {
        if (baseDir != null && autoDelete) {
            rmDir(baseDir);
        }
    }

    // recursively remove a directory, logging warnings if needed.
    private static void rmDir(File dir) {
        for (File file: dir.listFiles()) {
            if (file.isDirectory()) {
                rmDir(file);
            } else {
                if (!file.delete()) {
                    logger.warn("Can't delete file " + file);
                }
            }
        }
        if (!dir.delete()) {
            logger.warn("Can't delete dir " + dir);
        }
    }
}
