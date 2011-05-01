package com.github.cwilper.fcrepo.cloudsync.service.dao;

import com.github.cwilper.fcrepo.cloudsync.api.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfigurationDao extends AbstractDao {

    public ConfigurationDao(JdbcTemplate db) {
        super(db);
    }

    @Override
    public void initDb() {
        db.execute("create table Configuration (keepSysLogDays int not null, keepTaskLogDays int not null)");
        db.execute("insert into Configuration values (-1, -1)");
    }

    public Configuration getConfiguration() {
        return db.query("select * from Configuration",
                new ResultSetExtractor<Configuration>() {
                    @Override
                    public Configuration extractData(ResultSet rs)
                            throws SQLException {
                        rs.next();
                        Configuration c = new Configuration();
                        c.setKeepSysLogDays(rs.getInt("keepSysLogDays"));
                        c.setKeepTaskLogDays(rs.getInt("keepTaskLogDays"));
                        return c;
                    }
                });
    }

    public Configuration updateConfiguration(Configuration configuration) {
        Configuration updated = getConfiguration();
        if (configuration.getKeepSysLogDays() != null) {
            updated.setKeepSysLogDays(configuration.getKeepSysLogDays());
        }
        if (configuration.getKeepTaskLogDays() != null) {
            updated.setKeepTaskLogDays(configuration.getKeepTaskLogDays());
        }
        db.update("update Configuration set keepSysLogDays = ?, keepTaskLogDays = ?",
                updated.getKeepSysLogDays(), updated.getKeepTaskLogDays());
        return updated;
    }

}