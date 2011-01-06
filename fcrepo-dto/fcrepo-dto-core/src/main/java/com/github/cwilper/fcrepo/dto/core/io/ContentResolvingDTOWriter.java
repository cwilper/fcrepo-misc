package com.github.cwilper.fcrepo.dto.core.io;

import javax.annotation.PreDestroy;

public abstract class ContentResolvingDTOWriter implements DTOWriter {

    private final ContentResolver defaultContentResolver =
            new DefaultContentResolver();

    protected ContentResolver contentResolver;

    protected ContentResolvingDTOWriter() {
        contentResolver = defaultContentResolver;
    }

    public void setContentResolver(ContentResolver contentResolver) {
        if (contentResolver != defaultContentResolver
                && this.contentResolver == defaultContentResolver) {
            defaultContentResolver.close();
        }
        
    }

    @Override
    @PreDestroy
    public void close() {
        if (contentResolver == defaultContentResolver) {
            defaultContentResolver.close();
        }
    }

}
