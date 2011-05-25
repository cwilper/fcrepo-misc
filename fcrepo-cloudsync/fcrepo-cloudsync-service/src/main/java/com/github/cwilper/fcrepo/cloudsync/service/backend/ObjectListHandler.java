package com.github.cwilper.fcrepo.cloudsync.service.backend;

import com.github.cwilper.fcrepo.cloudsync.api.ObjectInfo;

public interface ObjectListHandler {

    boolean handleObject(ObjectInfo info);

}
