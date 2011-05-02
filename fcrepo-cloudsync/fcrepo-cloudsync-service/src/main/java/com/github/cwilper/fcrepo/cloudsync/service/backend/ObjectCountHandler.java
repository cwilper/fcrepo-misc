package com.github.cwilper.fcrepo.cloudsync.service.backend;

public interface ObjectCountHandler {

    void handleCountUpdate(long totalSoFar, boolean finished);
}
