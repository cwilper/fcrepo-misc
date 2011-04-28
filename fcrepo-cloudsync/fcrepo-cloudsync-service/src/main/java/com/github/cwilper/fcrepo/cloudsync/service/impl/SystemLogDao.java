package com.github.cwilper.fcrepo.cloudsync.service.impl;

import com.github.cwilper.fcrepo.cloudsync.api.SystemLog;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

class SystemLogDao {

    private final JdbcTemplate db;

    public SystemLogDao(JdbcTemplate db) {
        this.db = db;
    }

    public List<SystemLog> listSystemLogs() {
        List<SystemLog> list = new ArrayList<SystemLog>();
        SystemLog item = new SystemLog();
        item.id = "1";
        list.add(item);
        return list;
    }

    public SystemLog getSystemLog(String id) {
        SystemLog item = new SystemLog();
        item.id = id;
        return item;
    }

    public InputStream getSystemLogContent(String id) {
        try {
            return new ByteArrayInputStream(("System log " + id + " content").getBytes("UTF-8"));
        } catch (UnsupportedEncodingException wontHappen) {
            throw new RuntimeException(wontHappen);
        }
    }

    public void deleteSystemLog(String id) {
    }
}