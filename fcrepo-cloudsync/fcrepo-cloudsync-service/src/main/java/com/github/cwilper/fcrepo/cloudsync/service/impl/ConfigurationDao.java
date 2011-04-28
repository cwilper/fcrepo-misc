package com.github.cwilper.fcrepo.cloudsync.service.impl;

import com.github.cwilper.fcrepo.cloudsync.api.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

class ConfigurationDao {

    private final JdbcTemplate db;

    public ConfigurationDao(JdbcTemplate db) {
        this.db = db;
    }

    public Configuration getConfiguration() {
        return db.query("select * from Configuration",
                new ResultSetExtractor<Configuration>() {
                    public Configuration extractData(ResultSet rs)
                            throws SQLException {
                        rs.next();
                        Configuration c = new Configuration();
                        c.keepSysLogDays = rs.getInt("keepSysLogDays");
                        c.keepTaskLogDays = rs.getInt("keepTaskLogDays");
                        return c;
                    }
                });
    }

    public Configuration updateConfiguration(Configuration configuration) {
        Configuration updated = getConfiguration();
        if (configuration.keepSysLogDays != null) {
            updated.keepSysLogDays = configuration.keepSysLogDays;
        }
        if (configuration.keepTaskLogDays != null) {
            updated.keepTaskLogDays = configuration.keepTaskLogDays;
        }
        db.update("update Configuration set keepSysLogDays = ?, keepTaskLogDays = ?",
                updated.keepSysLogDays, updated.keepTaskLogDays);
        return updated;
    }

}