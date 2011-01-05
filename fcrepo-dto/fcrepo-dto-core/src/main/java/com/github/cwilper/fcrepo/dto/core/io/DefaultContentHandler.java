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

public class DefaultContentHandler implements ContentHandler {

    private static Logger logger = LoggerFactory.getLogger(
            DefaultContentHandler.class);

    private File baseDir;
    private boolean autoDelete;

    public DefaultContentHandler() {
        autoDelete = true;
    }

    public void setBaseDir(File baseDir) {
        if (this.baseDir != null && this.baseDir != baseDir && autoDelete) {
            rmDir(this.baseDir);
        }
        this.baseDir = baseDir;
    }

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
            sb.append("nopid:" + Math.abs(obj.hashCode()));
        }
        sb.append("/");
        sb.append(ds.id());
        sb.append("+");
        sb.append(dsv.id());
        return sb.toString();
    }

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
