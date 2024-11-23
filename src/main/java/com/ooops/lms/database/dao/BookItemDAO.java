package com.ooops.lms.database.dao;

import com.mysql.cj.util.LRUCache;
import com.ooops.lms.database.Database;
import com.ooops.lms.model.BookItem;
import com.ooops.lms.model.enums.BookItemStatus;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class BookItemDAO implements DatabaseQuery<BookItem> {
    private static Database database;
    private static BookDAO bookDAO;
    private static BookItemDAO bookItemDAO;
    private static LRUCache<Integer, BookItem> bookItemCache;

    private BookItemDAO() {
        database = Database.getInstance();
        bookDAO = BookDAO.getInstance();
        bookItemCache = new LRUCache<>(100);
    }

    public static synchronized BookItemDAO getInstance() {
        if (bookItemDAO == null) {
            bookItemDAO = new BookItemDAO();
        }
        return bookItemDAO;
    }

    //cahce
    // delete
    private static final String DELETE_BOOK_ITEM = "DELETE FROM BookItem WHERE barcode = ?";

    // find
    private static final String FIND_BOOK_ITEM
            = "SELECT * FROM BookItem bi join Books b on bi.ISBN = b.ISBN WHERE barcode = ?";

    // update
    private static final String UPDATE_BOOK_ITEM
            = "UPDATE BookItem SET BookItemStatus = ?, note = ?, ISBN = ? WHERE barcode = ?";

    // select all
    private static final String SELECT_ALL = "SELECT * FROM BookItem";

    /**
     * Thêm new bookitem
     *
     * @param entity new bookitem
     * @throws SQLException
     */
    @Override
    public void add(BookItem entity) throws SQLException {

    }

    /**
     * thay đổi thông tin bookitem.
     *
     * @param entity bookitem khi sửa xong thông tin
     * @return true nếu thành công và ngược lại
     * @throws SQLException
     */
    @Override
    public boolean update(@NotNull BookItem entity) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(UPDATE_BOOK_ITEM)) {
            preparedStatement.setString(1, entity.getStatus().name());
            preparedStatement.setString(2, entity.getNote());
            preparedStatement.setLong(3, entity.getISBN());
            preparedStatement.setInt(4, entity.getBarcode());

            if (preparedStatement.executeUpdate() > 0) {
                bookItemCache.put(entity.getBarcode(), entity);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean delete(@NotNull BookItem entity) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(DELETE_BOOK_ITEM)) {
            preparedStatement.setInt(1, entity.getBarcode());
            if (preparedStatement.executeUpdate() > 0) {
                bookItemCache.remove(entity.getBarcode());
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public BookItem find(Number keywords) throws SQLException {
        if (bookItemCache.containsKey(keywords.intValue())) {
            return bookItemCache.get(keywords.intValue());
        }
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(FIND_BOOK_ITEM)) {
            preparedStatement.setInt(1, (Integer) keywords);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    BookItem bookItem = new BookItem(resultSet.getInt("barcode")
                            , BookItemStatus.valueOf(resultSet.getString("BookItemStatus"))
                            , resultSet.getString("note"), bookDAO.find(resultSet.getLong("ISBN")));

                    bookItemCache.put(bookItem.getBarcode(), bookItem);
                    return bookItem;
                } else {
                    throw new SQLException("No book item found");
                }
            }
        }
    }

    @Override
    public List<BookItem> searchByCriteria(@NotNull Map<String, Object> criteria) throws SQLException {

        StringBuilder findBookByCriteria = new StringBuilder("Select * from bookitem bi join books b on bi.ISBN = b.ISBN where ");

        List<BookItem> bookItemList = new ArrayList<>();

        for (String key : criteria.keySet()) {
            if (key.equals("ISBN")) {
                findBookByCriteria.append("CAST(bi.ISBN AS CHAR)").append(" LIKE ? AND ");
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
                return bookItemList;
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

    @NotNull
    private String generateKeywords(@NotNull Map<String, Object> criteria) {
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
