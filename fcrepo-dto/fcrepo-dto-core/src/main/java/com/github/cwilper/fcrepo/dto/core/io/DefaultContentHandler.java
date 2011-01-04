package com.github.cwilper.fcrepo.dto.core.io;

import com.github.cwilper.fcrepo.dto.core.Datastream;
import com.github.cwilper.fcrepo.dto.core.DatastreamVersion;
import com.github.cwilper.fcrepo.dto.core.FedoraObject;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

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
    public void handleContent(FedoraObject obj,
                              Datastream ds,
                              DatastreamVersion dsv,
                              InputStream source) throws IOException {
        dsv.contentLocation(store(obj.pid() + "/" + ds.id() + "+" + dsv.id(),
                source));
    }

    private URI store(String path, InputStream source) throws IOException {
        FileOutputStream sink = null;
        try {
            File file = new File(baseDir(), path);
            if (!file.getParentFile().mkdirs()) {
                throw new IOException("Can't create parent dir of " + file);
            }
            sink = new FileOutputStream(file);
            IOUtils.copy(source, sink);
            return file.toURI();
        } finally {
            IOUtils.closeQuietly(source);
            IOUtils.closeQuietly(sink);
        }
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
