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
    private Database database;
    private BookDAO bookDAO;
    private static BookItemDAO bookItemDAO;

    private BookItemDAO() {
        database = Database.getInstance();
        bookDAO = BookDAO.getInstance();
    }

    public static BookItemDAO getInstance() {
        if (bookItemDAO == null) {
            bookItemDAO = new BookItemDAO();
        }
        return bookItemDAO;
    }
    //cahce
    private static final int MAX_CACHE_SIZE = 100;
    private LRUCache<String, List<BookItem>> bookItemCache = new LRUCache<>(MAX_CACHE_SIZE);

    // delete
    private static final String DELETE_BOOK_ITEM = "DELETE FROM BookItem WHERE barcode = ?";

    // find
    private static final String FIND_BOOK_ITEM
            = "SELECT * FROM BookItem bi join Books b on bi.ISBN = b.ISBN WHERE barcode = ?";

    // update
    private static final String UPDATE_BOOK_ITEM
            = "UPDATE BookItem SET status = ?, note = ?, ISBN = ? WHERE barcode = ?";

    // select all
    private static final String SELECT_ALL = "SELECT * FROM BookItem";

    /**
     * Thêm new bookitem
     * @param entity new bookitem
     * @throws SQLException
     */
    @Override
    public void add(BookItem entity) throws SQLException {

    }

    /**
     * thay đổi thông tin bookitem.
     * @param entity bookitem khi sửa xong thông tin
     * @return true nếu thành công và ngược lại
     * @throws SQLException
     */
    @Override
    public boolean update(BookItem entity) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(UPDATE_BOOK_ITEM)) {
            preparedStatement.setString(1, entity.getStatus().name());
            preparedStatement.setString(2, entity.getNote());
            preparedStatement.setLong(3, entity.getISBN());
            preparedStatement.setInt(4, entity.getBarcode());

            return preparedStatement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(BookItem entity) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(DELETE_BOOK_ITEM)) {
            preparedStatement.setInt(1, entity.getBarcode());
            return preparedStatement.executeUpdate() > 0;
        }
    }

    @Override
    public BookItem find(Number keywords) throws SQLException {

        if (bookItemCache.containsKey(String.valueOf(keywords))) {
            return bookItemCache.get(String.valueOf(keywords)).get(0);
        }

        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(FIND_BOOK_ITEM)) {
            preparedStatement.setInt(1, (Integer) keywords);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    BookItem bookItem = new BookItem(resultSet.getInt("barcode")
                            , BookItemStatus.valueOf(resultSet.getString("status"))
                            , resultSet.getString("note"), bookDAO.find(resultSet.getLong("ISBN")));

                    List<BookItem> bookItemList = new ArrayList<>();
                    bookItemList.add(bookItem);
                    bookItemCache.put(String.valueOf(keywords), bookItemList);
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
                if (criteria.get(key) == "ISBN") {
                    findBookByCriteria.append("CAST(").append(key).append(" AS CHAR)").append(" LIKE ? AND ");
                } else {
                    findBookByCriteria.append(key).append(" LIKE ? AND ");
                }
            }

            findBookByCriteria.setLength(findBookByCriteria.length() - 5);

            try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(findBookByCriteria.toString())) {
                int index = 1;

                for (Object value : criteria.values()) {
                    preparedStatement.setString(index++, "%" + value + "%");
                }

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        bookItemList.add(find(resultSet.getInt("barcode")));
                    }
                    bookItemCache.put(keywords, bookItemList);
                    return bookItemList;
                }
            }
        }
    }

    // Không sử dụng
    @Override
    public List<BookItem> selectAll() throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(SELECT_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<BookItem> bookItemList = new ArrayList<>();
            while (resultSet.next()) {
                bookItemList.add(find(resultSet.getInt("barcode")));
            }
            return bookItemList;
        }
    }

    private String generateKeywords(Map<String, Object> criteria) {
        StringBuilder keywords = new StringBuilder();

        for (Map.Entry<String, Object> entry : criteria.entrySet()) {
            if (entry.getValue() != null) {
                keywords.append(entry.getKey()).append(": ").append(entry.getValue().toString()).append(" ");
            }
        }

        if (!keywords.isEmpty()) {
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
