package com.ooops.lms.database.dao;

import com.ooops.lms.database.Database;
import com.ooops.lms.model.enums.BookItemStatus;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class BookIssueDAO implements DatabaseQuery {
    private Database database;
    private BookItemStatus status;

    @Override
    public void add(Object entity) throws SQLException {

    }

    @Override
    public boolean update(Object entity) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(Object entity) throws SQLException {
        return false;
    }

    @Override
    public Object find(Number keywords) throws SQLException {
        return null;
    }

    @Override
    public List selectAll() throws SQLException {
        return List.of();
    }

    @Override
    public List searchByCriteria(Map criteria) throws SQLException {
        return List.of();
    }
}
