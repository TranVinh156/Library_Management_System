package com.ooops.lms.database.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DatabaseQuery<T> {
    void add(T entity) throws SQLException;
    boolean update(T entity) throws SQLException;
    boolean delete(T entity) throws SQLException;
    T find(String keywords) throws SQLException;
    List<T> searchByCriteria(Map<String, Object> criteria) throws SQLException;
}
