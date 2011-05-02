package com.github.cwilper.fcrepo.cloudsync.service.backend;

import com.github.cwilper.fcrepo.cloudsync.api.ObjectInfo;

public interface ObjectListResultHandler {

    // if no results, one callback will be made with info null, finished true
    void handleObject(ObjectInfo info, boolean finished);
}
