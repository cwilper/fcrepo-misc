package com.github.cwilper.fcrepo.cloudsync.service.impl;

import org.springframework.jdbc.core.JdbcTemplate;

abstract class AbstractDao {

    protected final JdbcTemplate db;

    public AbstractDao(JdbcTemplate db) {
        this.db = db;
    }

    public abstract void initDb();
}
