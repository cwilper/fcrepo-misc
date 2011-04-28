package com.github.cwilper.fcrepo.cloudsync.service.impl;

import com.github.cwilper.fcrepo.cloudsync.api.User;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.context.SecurityContextHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class UserDao {

    private final JdbcTemplate db;

    public UserDao(JdbcTemplate db) {
        this.db = db;
    }

    // returns null if the user name conflicts with an existing one
    public User createUser(final User user) {
        if (user.name == null || user.name.trim().length() == 0) {
            throw new IllegalArgumentException("User.name must be specified");
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            db.update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection conn)
                        throws SQLException {
                    PreparedStatement ps = conn.prepareStatement(
                            "insert into Users (name) values (?)",
                            new String[] { "ID" }); // must be caps
                    ps.setString(1, user.name.trim());
                    return ps;
                }
            }, keyHolder);
            return getUser(keyHolder.getKey().toString());
        } catch (DuplicateKeyException e) {
            return null;
        }
    }

    public List<User> listUsers() {
        List<User> list = new ArrayList<User>();
        User user = new User();
        user.id = "1";
        list.add(user);
        return list;
    }

    public User getUser(String id) {
        return db.query("select * from Users where id = ?",
                new ResultSetExtractor<User>() {
                    public User extractData(ResultSet rs)
                            throws SQLException {
                        if (rs.next()) {
                            User u = new User();
                            u.id = "" + rs.getInt("id");
                            u.name = rs.getString("name");
                            return u;
                        } else {
                            return null;
                        }
                    }
                },
                Integer.parseInt(id));
    }

    public User getCurrentUser() {
        User user = new User();
        user.id = "9";
        org.springframework.security.core.userdetails.User u =
                (org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.name = u.getUsername();
        return user;
    }

    public User updateUser(String id, User user) {
        User u = new User();
        u.id = user.id;
        return u;
    }

    public void deleteUser(String id) {
    }

}