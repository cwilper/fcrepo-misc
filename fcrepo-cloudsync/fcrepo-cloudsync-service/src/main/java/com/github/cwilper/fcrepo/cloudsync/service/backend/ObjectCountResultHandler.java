package com.github.cwilper.fcrepo.cloudsync.service.backend;

public interface ObjectCountResultHandler {

    void handleCountUpdate(long totalSoFar, boolean finished);
}
