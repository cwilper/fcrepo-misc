package com.github.cwilper.fcrepo.dto.core.io;

import javax.annotation.PreDestroy;

/**
 * Base class for {@link DTOReader} implementations capable of handling
 * content.
 */
public abstract class ContentHandlingDTOReader implements DTOReader {

    private final ContentHandler defaultContentHandler =
            new DefaultContentHandler();
    
    protected ContentHandler contentHandler;

    /**
     * Creates an instance with a {@link DefaultContentHandler} which
     * is guaranteed to be closed by the time this reader is closed.
     *
     * @see DefaultContentHandler
     */
    public ContentHandlingDTOReader() {
        contentHandler = defaultContentHandler;
    }

    /**
     * Sets the content handler. If the new content handler is different
     * from the default, and the default is still in use, it will be
     * automatically closed.
     *
     * @param contentHandler the new value, never <code>null</code>.
     * @throws NullPointerException if the value is null.
     */
    public void setContentHandler(ContentHandler contentHandler) {
        if (contentHandler == null) throw new NullPointerException();
        if (contentHandler != defaultContentHandler
                && this.contentHandler == defaultContentHandler) {
            defaultContentHandler.close();
        }
    }

    /**
     * Gets the content handler.
     *
     * @return the value, never <code>null</code>.
     */
    public ContentHandler getContentHandler() {
        return contentHandler;
    }

    @Override
    @PreDestroy
    public void close() {
        if (contentHandler == defaultContentHandler) {
            defaultContentHandler.close();
        }
    }
}
