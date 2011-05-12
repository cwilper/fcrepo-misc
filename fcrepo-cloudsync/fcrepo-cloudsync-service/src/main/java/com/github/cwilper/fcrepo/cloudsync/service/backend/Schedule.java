package com.github.cwilper.fcrepo.cloudsync.service.backend;

import com.github.cwilper.fcrepo.cloudsync.service.util.JSON;

import java.util.Map;

public class Schedule {

    public Schedule(String json) {
        Map<String, String> map = JSON.getMap(JSON.parse(json));
        // TODO: validate
    }
}
