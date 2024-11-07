package com.ooops.lms.database.dao;

import com.ooops.lms.database.Database;
import com.ooops.lms.model.BookReservation;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class BookReservationDAO implements DatabaseQuery<BookReservation> {
    private Database database;

    public BookReservationDAO() {
        database = Database.getInstance();
    }

    @Override
    public void add(BookReservation entity) throws SQLException {

    }

    @Override
    public boolean update(BookReservation entity) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(BookReservation entity) throws SQLException {
        return false;
    }

    @Override
    public BookReservation find(Number keywords) throws SQLException {
        return null;
    }

    @Override
    public List<BookReservation> searchByCriteria(Map<String, Object> criteria) throws SQLException {
        return List.of();
    }

    @Override
    public List<BookReservation> selectAll() throws SQLException {
        return List.of();
    }
}
