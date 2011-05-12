package com.github.cwilper.fcrepo.cloudsync.api;

public class NameConflictException extends Exception {

    public NameConflictException(String message) {
        super(message);
    }

    public NameConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
