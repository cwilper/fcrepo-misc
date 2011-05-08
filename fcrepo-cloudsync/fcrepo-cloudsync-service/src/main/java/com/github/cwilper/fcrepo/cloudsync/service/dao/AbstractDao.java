package com.github.cwilper.fcrepo.cloudsync.service.dao;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

abstract class AbstractDao {

    protected final JdbcTemplate db;

    public AbstractDao(JdbcTemplate db) {
        this.db = db;
    }

    public abstract void initDb();

    protected String validateString(String name, String value, int maxLen)
            throws IllegalArgumentException {
        if (value == null || value.trim().length() == 0) {
            throw new IllegalArgumentException("A value must be specified for"
                    + " '" + name + "'");
        } else if (value.trim().length() > maxLen) {
            throw new IllegalArgumentException("The value specified for"
                    + " '" + name + "' was too long. It must not exceed"
                    + " " + maxLen + " characters.");
        }
        return value.trim();
    }

    protected String validateString(String name, String value, String[] validValues)
            throws IllegalArgumentException {
        String normalized = validateString(name, value, Integer.MAX_VALUE);
        boolean found = false;
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < validValues.length; i++) {
            if (i > 0) {
                s.append(", ");
            }
            s.append("'");
            s.append(validValues[i]);
            s.append("'");
            if (normalized.equals(validValues[i])) return normalized;
        }
        throw new IllegalArgumentException("One of the following values must"
                + " be specified for '" + name + "': [ " + s.toString()
                + " ]");
    }

    protected String insert(final String sql, final Object... values) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            db.update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection conn)
                        throws SQLException {
                    PreparedStatement ps = conn.prepareStatement(sql,
                            new String[] { "ID" }); // must be caps
                    for (int i = 1; i <= values.length; i++) {
                        // Value may be null, so here we depend on Derby's
                        // ability to handle this in version 10.7.1.1+
                        // https://issues.apache.org/jira/browse/DERBY-1938
                        ps.setObject(i, values[i-1]);
                    }
                    return ps;
                }
            }, keyHolder);
            return keyHolder.getKey().toString();
        } catch (DuplicateKeyException e) {
            return null;
        }
    }
}
