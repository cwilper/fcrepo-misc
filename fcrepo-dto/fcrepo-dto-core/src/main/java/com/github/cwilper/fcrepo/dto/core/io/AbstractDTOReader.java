package com.github.cwilper.fcrepo.dto.core.io;

import javax.annotation.PreDestroy;

public abstract class AbstractDTOReader implements DTOReader {

    private final ContentHandler defaultContentHandler =
            new DefaultContentHandler();
    
    protected ContentHandler contentHandler;

    public AbstractDTOReader() {
        contentHandler = defaultContentHandler;
    }

    public void setContentHandler(ContentHandler contentHandler) {
        if (contentHandler != defaultContentHandler
                && this.contentHandler == defaultContentHandler) {
            defaultContentHandler.close();
        }
    }

    @Override
    @PreDestroy
    public void close() {
        if (contentHandler == defaultContentHandler) {
            defaultContentHandler.close();
        }
    }
}
