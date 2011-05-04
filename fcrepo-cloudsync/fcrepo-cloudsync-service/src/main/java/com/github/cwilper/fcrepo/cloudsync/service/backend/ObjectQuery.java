package com.github.cwilper.fcrepo.cloudsync.service.backend;

import com.github.cwilper.fcrepo.cloudsync.api.ObjectSet;
import com.github.cwilper.fcrepo.cloudsync.service.util.JSON;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ObjectQuery {

    private final String type;
    private final String pidPattern;
    private final List<String> pidList;
    private final String rdfQueryType;
    private final String rdfQueryText;

    public ObjectQuery(ObjectSet set) {
        this.type = set.getType();
        // TODO: Validate name and data non-empty too?
        if (type == null || type.trim().length() == 0) {
            throw new IllegalArgumentException("ObjectSet type not specified");
        }
        if (type.equals("pidPattern")) {
            // TODO: validate?
            pidPattern = set.getData();
            pidList = null;
            rdfQueryType = null;
            rdfQueryText = null;
        } else if (type.equals("pidList")) {
            // TODO: validate?
            pidPattern = null;
            pidList = new ArrayList<String>();
            String[] pids = set.getData().split("\\S");
            for (String pid: pids) {
                pidList.add(pid);
            }
            rdfQueryType = null;
            rdfQueryText = null;
        } else if (type.equals("rdfQuery")) {
            // TODO: validate?
            pidPattern = null;
            pidList = null;
            Map<String, String> map = JSON.getMap(JSON.parse(set.getData()));
            rdfQueryType = map.get("rdfQueryType");
            rdfQueryText = map.get("rdfQueryText");
        } else {
            throw new IllegalArgumentException("Unrecognized ObjectSet type: " + type);
        }
    }

    public String getType() {
        return this.type;
    }

    public String getPidPattern() {
        return pidPattern;
    }

    public List<String> getPidList() {
        return pidList;
    }

    public String getRdfQueryType() {
        return rdfQueryType;
    }

    public String getRdfQueryText() {
        return rdfQueryText;
    }

}
