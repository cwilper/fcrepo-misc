package com.github.cwilper.fcrepo.cloudsync.service.backend;

import java.util.List;
import java.util.Map;

public class ObjectQuery {

    private final String type;
    private final Map<String, String> args;
    private final List<String> pids;

    public ObjectQuery(String type, String data) {
        this.type = type;
        if (type.equals("all")) {
            this.args = null;
            this.pids = null;
        } else if (type.equals("pidlist")) {
            this.args = null;
            this.pids = null; // TODO: split by whitespace
        } else {
            this.args = null; // TODO: parse as json map
            this.pids = null;
        }
    }

    public Map<String, String> getArgs() {
        return args;
    }

    public List<String> getPids() {
        return pids;
    }

}
