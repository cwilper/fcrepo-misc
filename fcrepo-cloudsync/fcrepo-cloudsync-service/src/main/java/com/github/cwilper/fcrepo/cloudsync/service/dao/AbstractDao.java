package com.github.cwilper.fcrepo.cloudsync.service.dao;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;

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

    protected JsonNode validateJson(String name, String value)
            throws IllegalArgumentException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(value, JsonNode.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Malformed JSON specified for"
                    + "'" + name + "'", e);
        }
    }
}