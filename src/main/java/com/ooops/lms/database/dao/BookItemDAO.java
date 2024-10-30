package com.ooops.lms.database.dao;

import com.mysql.cj.util.LRUCache;
import com.ooops.lms.database.Database;
import com.ooops.lms.model.BookItem;
import com.ooops.lms.model.enums.BookItemStatus;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class BookItemDAO implements DatabaseQuery<BookItem> {
    private Database database = Database.getInstance();

    //cahce
    private LRUCache<String, List<BookItem>> bookItemCache = new LRUCache<>(20);

    // delete
    private static final String DELETE_BOOK_ITEM = "DELETE FROM book_item WHERE barcode = ?";

    // find
    private static final String FIND_BOOK_ITEM = "SELECT * FROM book_item WHERE barcode = ?";
    private static final String FIND_BOOK_BY_CRITERIA = "SELECT * FROM book_item WHERE ";

    @Override
    public void add(BookItem entity) throws SQLException {

    }

    @Override
    public boolean update(BookItem entity) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(BookItem entity) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(DELETE_BOOK_ITEM)) {
            preparedStatement.setInt(1, entity.getBarcode());
            return preparedStatement.executeUpdate() > 0;
        }
    }

    @Override
    public BookItem find(String keywords) throws SQLException {

        if (bookItemCache.containsKey(keywords)) {
            return bookItemCache.get(keywords).get(0);
        }

        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(FIND_BOOK_ITEM)) {
            preparedStatement.setString(1, keywords);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    BookItem bookItem = new BookItem(resultSet.getInt("barcode")
                            , resultSet.getInt("ISBN")
                            , BookItemStatus.valueOf(resultSet.getString("status"))
                            , resultSet.getString("note"));

                    List<BookItem> bookItemList = new ArrayList<>();
                    bookItemList.add(bookItem);
                    bookItemCache.put(keywords, bookItemList);
                    return bookItem;
                } else {
                    throw new SQLException("No book item found");
                }
            }
        }
    }

    @Override
    public List<BookItem> searchByCriteria(Map<String, Object> criteria) throws SQLException {
        String keywords = generateKeywords(criteria);

        StringBuilder findBookByCriteria = new StringBuilder("Select * from book_item where ");

        if (bookItemCache.containsKey(keywords)) {
            return bookItemCache.get(keywords);
        } else {
            List<BookItem> bookItemList = new ArrayList<>();

            for (String key : criteria.keySet()) {
                findBookByCriteria.append(key).append(" LIKE ? AND ");
            }

            findBookByCriteria.setLength(findBookByCriteria.length() - 5);

            try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(findBookByCriteria.toString())) {
                int index = 1;

                for (Object value : criteria.values()) {
                    preparedStatement.setString(index++, "%" + value + "%");
                }

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        BookItem bookItem = new BookItem(resultSet.getInt("barcode")
                                , resultSet.getInt("ISBN")
                                , BookItemStatus.valueOf(resultSet.getString("status"))
                                , resultSet.getString("note"));
                        bookItemList.add(bookItem);
                    }
                    bookItemCache.put(keywords, bookItemList);
                    return bookItemList;
                }
            }
        }
    }

    private String generateKeywords(Map<String, Object> criteria) {
        StringBuilder keywords = new StringBuilder();

        for (Map.Entry<String, Object> entry : criteria.entrySet()) {
            if (entry.getValue() != null) {
                keywords.append(entry.getKey()).append(": ").append(entry.getValue().toString()).append(" ");
            }
        }

        if (keywords.length() > 0) {
            keywords.setLength(keywords.length() - 1);
        }

        return keywords.toString();
    }

    public static void main(String[] args) {
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("barcode", "123");
        criteria.put("status", BookItemStatus.AVAILABLE);
        BookItemDAO bookItemDAO = new BookItemDAO();
        String keywords = bookItemDAO.generateKeywords(criteria);
        System.out.println(keywords);

        StringBuilder findBookByCriteria = new StringBuilder("Select * from book_item where ");

        for (Map.Entry<String, Object> entry : criteria.entrySet()) {
            findBookByCriteria.append(entry.getKey()).append(" like '%").append(entry.getValue()).append("%'").append(" and ");
        }
        findBookByCriteria.setLength(findBookByCriteria.length() - 5);

        System.out.println(findBookByCriteria.toString());
    }
}
