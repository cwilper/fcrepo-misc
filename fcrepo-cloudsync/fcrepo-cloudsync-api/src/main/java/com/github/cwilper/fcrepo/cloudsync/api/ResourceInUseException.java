package com.github.cwilper.fcrepo.cloudsync.api;

public class ResourceInUseException extends Exception {

    public ResourceInUseException(String message) {
        super(message);
    }

    public ResourceInUseException(String message, Throwable cause) {
        super(message, cause);
    }
}
