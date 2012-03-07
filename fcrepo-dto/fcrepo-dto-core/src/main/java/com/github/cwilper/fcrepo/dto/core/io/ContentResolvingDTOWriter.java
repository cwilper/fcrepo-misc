package com.github.cwilper.fcrepo.dto.core.io;

import javax.annotation.PreDestroy;
import java.net.URI;

/**
 * Base class of {@link DTOWriter} implementations capable of resolving
 * content.
 */
public abstract class ContentResolvingDTOWriter implements DTOWriter {

    protected final ContentResolver defaultContentResolver =
            new DefaultContentResolver();

    protected ContentResolver contentResolver;
    protected URI baseURI;

    /**
     * Creates an instance with a {@link DefaultContentResolver} which is
     * guaranteed to be closed by the time the writer is closed.
     */
    protected ContentResolvingDTOWriter() {
        contentResolver = defaultContentResolver;
    }

    /**
     * Sets the content resolver. If the new content resolver is different
     * from the default, and the default is still in use, it will be
     * automatically closed.
     *
     * @param contentResolver the new value, never <code>null</code>.
     * @throws NullPointerException if the value is null.
     */
    public void setContentResolver(ContentResolver contentResolver) {
        if (contentResolver != defaultContentResolver
                && this.contentResolver == defaultContentResolver) {
            defaultContentResolver.close();
        }
        
    }

    /**
     * Gets the content resolver.
     *
     * @return the value, never <code>null</code>.
     */
    public ContentResolver getContentResolver() {
        return contentResolver;
    }

    /**
     * Sets the base URI to use when relative URIs need to be resolved.
     *
     * @param baseURI the new value, possibly <code>null</code>.
     */
    public void setBaseURI(URI baseURI) {
        this.baseURI = baseURI;
    }

    @Override
    @PreDestroy
    public void close() {
        if (contentResolver == defaultContentResolver) {
            defaultContentResolver.close();
        }
    }

}
