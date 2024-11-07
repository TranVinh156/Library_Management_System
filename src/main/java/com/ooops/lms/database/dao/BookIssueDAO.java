package com.ooops.lms.database.dao;

import com.ooops.lms.database.Database;
import com.ooops.lms.model.BookIssue;
import com.ooops.lms.model.enums.BookItemStatus;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class BookIssueDAO implements DatabaseQuery<BookIssue> {
    private Database database;
    private BookItemStatus status;


    @Override
    public void add(BookIssue entity) throws SQLException {

    }

    @Override
    public boolean update(BookIssue entity) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(BookIssue entity) throws SQLException {
        return false;
    }

    @Override
    public BookIssue find(Number keywords) throws SQLException {
        return null;
    }

    @Override
    public List<BookIssue> searchByCriteria(Map<String, Object> criteria) throws SQLException {
        return List.of();
    }

    @Override
    public List<BookIssue> selectAll() throws SQLException {
        return List.of();
    }
}
