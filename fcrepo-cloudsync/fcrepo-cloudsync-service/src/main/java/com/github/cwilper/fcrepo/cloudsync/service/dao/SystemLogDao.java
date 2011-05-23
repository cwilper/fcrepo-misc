package com.github.cwilper.fcrepo.cloudsync.service.dao;

import com.github.cwilper.fcrepo.cloudsync.api.SystemLog;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class SystemLogDao extends AbstractDao {

    public SystemLogDao(JdbcTemplate db) {
        super(db);
    }

    @Override
    public void initDb() {
        // TODO: Implement me
    }

    public List<SystemLog> listSystemLogs() {
        List<SystemLog> list = new ArrayList<SystemLog>();
        SystemLog item = new SystemLog();
        item.setId("1");
        list.add(item);
        return list;
    }

    public SystemLog getSystemLog(String id) {
        SystemLog item = new SystemLog();
        item.setId(id);
        return item;
    }

    public InputStream getSystemLogContent(String id) throws FileNotFoundException {
        throw new FileNotFoundException();
        /*
        try {
   //         return new ByteArrayInputStream(("System log " + id + " content").getBytes("UTF-8"));
        } catch (UnsupportedEncodingException wontHappen) {
            throw new RuntimeException(wontHappen);
        }
        */
    }

    public void deleteSystemLog(String id) {
    }
}