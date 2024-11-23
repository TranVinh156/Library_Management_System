package com.ooops.lms.database.dao;

import com.mysql.cj.util.LRUCache;
import com.ooops.lms.database.Database;
import com.ooops.lms.model.BookReservation;
import com.ooops.lms.model.enums.BookReservationStatus;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BookReservationDAO implements DatabaseQuery<BookReservation> {
    private static BookReservationDAO bookReservationDAO;

    private static Database database;
    private static MemberDAO memberDAO;
    private static BookItemDAO bookItemDAO;
    private static LRUCache<Integer, BookReservation> bookReservationCache;

    private BookReservationDAO() {
        database = Database.getInstance();
        memberDAO = MemberDAO.getInstance();
        bookItemDAO = BookItemDAO.getInstance();
        bookReservationCache = new LRUCache<>(100);
    }

    public static synchronized BookReservationDAO getInstance() {
        if (bookReservationDAO == null) {
            bookReservationDAO = new BookReservationDAO();
        }
        return bookReservationDAO;
    }

    // add
    private static final String ADD_BOOK_RESERVATION
            = "Insert into BookReservation(member_ID, barcode, creation_date, due_date) values (?, ?, ?, ?)";

    // update
    private static final String UPDATE_BOOK_RESERVATION
            = "Update BookReservation set member_ID = ?, barcode = ?, creation_date = ?, due_date = ?, BookReservationStatus = ? where reservation_ID = ?";

    // delete
    private static final String DELETE_BOOK_RESERVATION
            = "Delete FROM BookReservation where reservation_ID = ?";

    // find
    private static final String FIND_BOOK_RESERVATION = "Select * from BookReservation where reservation_ID = ?";

    // select all
    private static final String SELECT_ALL = "Select * from BookReservation";

    @Override
    public void add(@NotNull BookReservation entity) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(ADD_BOOK_RESERVATION, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, entity.getMember().getPerson().getId());
            preparedStatement.setInt(2, entity.getBookItem().getBarcode());
            preparedStatement.setString(3, entity.getCreatedDate());
            preparedStatement.setString(4, entity.getDueDate());

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                entity.setReservationId(resultSet.getInt(1));
            }
            bookReservationCache.put(entity.getReservationId(), entity);
        }
    }

    @Override
    public boolean update(@NotNull BookReservation entity) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(UPDATE_BOOK_RESERVATION)) {
            preparedStatement.setInt(1, entity.getMember().getPerson().getId());
            preparedStatement.setInt(2, entity.getBookItem().getBarcode());
            preparedStatement.setString(3, entity.getCreatedDate());
            preparedStatement.setString(4, entity.getDueDate());
            preparedStatement.setString(5, entity.getStatus().name());
            preparedStatement.setInt(6, entity.getReservationId());

            if (preparedStatement.executeUpdate() > 0) {
                bookReservationCache.put(entity.getReservationId(), entity);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean delete(@NotNull BookReservation entity) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(DELETE_BOOK_RESERVATION)) {
            preparedStatement.setInt(1, entity.getReservationId());

            if (preparedStatement.executeUpdate() > 0) {
                bookReservationCache.remove(entity.getReservationId());
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public BookReservation find(@NotNull Number keywords) throws SQLException {
        if (bookReservationCache.containsKey(keywords.intValue())) {
            return bookReservationCache.get(keywords.intValue());
        }
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(FIND_BOOK_RESERVATION)) {
            preparedStatement.setInt(1, keywords.intValue());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    BookReservation bookReservation = new BookReservation(resultSet.getInt("reservation_ID")
                            , memberDAO.find(resultSet.getInt("member_ID"))
                            , bookItemDAO.find(resultSet.getInt("barcode"))
                            , resultSet.getString("creation_date")
                            , resultSet.getString("due_date")
                            , BookReservationStatus.valueOf(resultSet.getString("BookReservationStatus")));
                    bookReservationCache.put(bookReservation.getReservationId(), bookReservation);
                    return bookReservation;
                }
            }
        }
        return null;
    }

    @Override
    public List<BookReservation> searchByCriteria(@NotNull Map<String, Object> criteria) throws SQLException {
        StringBuilder findBookReservationByCriteria = new StringBuilder("Select distinct (reservation_ID) from BookReservation " +
                "join Members on BookReservation.member_ID = Members.member_ID " +
                "join BookItem on BookReservation.barcode = BookItem.barcode " +
                "join Books on Books.ISBN = BookItem.ISBN " +
                "where ");

        boolean[] flag = new boolean[15];
        int index = 1;

        for (String key : criteria.keySet()) {
            switch (key) {
                case "barcode" -> findBookReservationByCriteria.append("CAST(BookReservation.barcode AS CHAR) LIKE ? AND ");
                case "member_ID" -> findBookReservationByCriteria.append("CAST(BookReservation.member_ID AS CHAR) LIKE ? AND ");
                case "creation_date" -> {
                    flag[index] = true;
                    findBookReservationByCriteria.append("DATE(creation_date) = ? AND ");
                }
                case "due_date" -> {
                    flag[index] = true;
                    findBookReservationByCriteria.append("DATE(due_date) = ? AND ");
                }
                case "fullname" ->
                        findBookReservationByCriteria.append("CONCAT(Members.last_name, ' ', Members.first_name) LIKE ? AND ");
                default -> findBookReservationByCriteria.append(key).append(" LIKE ? AND ");
            }
            index ++;
        }

        findBookReservationByCriteria.setLength(findBookReservationByCriteria.length() - 5);

        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(findBookReservationByCriteria.toString())) {
            index = 1;
            for (Object value : criteria.values()) {
                if (!flag[index]) {
                    preparedStatement.setString(index++, "%" + value.toString() + "%");
                } else {
                    preparedStatement.setString(index++, value.toString());
                }
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<BookReservation> bookReservations = new ArrayList<>();
                while (resultSet.next()) {
                    bookReservations.add(find(resultSet.getInt("reservation_ID")));
                }
                return bookReservations;
            }
        }
    }

    @Override
    public List<BookReservation> selectAll() throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(SELECT_ALL)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<BookReservation> bookReservations = new ArrayList<>();
                while (resultSet.next()) {
                    bookReservations.add(find(resultSet.getInt("reservation_ID")));
                }
                return bookReservations;
            }
        }
    }
}
