package com.ooops.lms.database.dao;

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

    private Database database;
    private MemberDAO memberDAO;
    private BookItemDAO bookItemDAO;

    private BookReservationDAO() {
        database = Database.getInstance();
        memberDAO = MemberDAO.getInstance();
        bookItemDAO = BookItemDAO.getInstance();
    }

    public static BookReservationDAO getInstance() {
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
            = "Update BookReservation set member_ID = ?, barcode = ?, creation_date = ?, due_date = ?, status = ? where reservation_ID = ?";

    // delete
    private static final String DELETE_BOOK_RESERVATION
            = "Delete BookReservation where reservation_ID = ?";

    // find
    private static final String FIND_BOOK_RESERVATION = "Select * from BookReservation where reservation_ID = ?";

    // select all
    private static final String SELECT_ALL = "Select * from BookReservation";

    @Override
    public void add(@NotNull BookReservation entity) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(ADD_BOOK_RESERVATION)) {
            preparedStatement.setInt(1, entity.getMember().getPerson().getId());
            preparedStatement.setInt(2, entity.getBookItem().getBarcode());
            preparedStatement.setString(3, entity.getCreatedDate());
            preparedStatement.setString(4, entity.getDueDate());

            preparedStatement.executeUpdate();
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

            return preparedStatement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(@NotNull BookReservation entity) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(DELETE_BOOK_RESERVATION)) {
            preparedStatement.setInt(1, entity.getReservationId());
            return preparedStatement.executeUpdate() > 0;
        }
    }

    @Override
    public BookReservation find(@NotNull Number keywords) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(FIND_BOOK_RESERVATION)) {
            preparedStatement.setInt(1, keywords.intValue());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    BookReservation bookReservation = new BookReservation(resultSet.getInt("reservation_ID")
                            , memberDAO.find(resultSet.getInt("member_ID"))
                            , bookItemDAO.find(resultSet.getInt("barcode"))
                            , resultSet.getString("creation_date")
                            , resultSet.getString("due_date")
                            , BookReservationStatus.valueOf(resultSet.getString("status")));
                    return bookReservation;
                }
            }
        }
        return null;
    }

    @Override
    public List<BookReservation> searchByCriteria(@NotNull Map<String, Object> criteria) throws SQLException {
        StringBuilder findBookReservationByCriteria = new StringBuilder("Select * from BookReservation where ");
        Iterator<String> iterator = criteria.keySet().iterator();

        while (iterator.hasNext()) {
            String key = iterator.next();
            findBookReservationByCriteria.append(key).append(" LIKE ?");
            if (iterator.hasNext()) {
                findBookReservationByCriteria.append(" AND ");
            }
        }

        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(findBookReservationByCriteria.toString())) {
            int index = 1;

            for (Object value : criteria.values()) {
                if (value != null) {
                    preparedStatement.setString(index++, "%" + value.toString() + "%");
                } else {
                    preparedStatement.setString(index++, "%");
                }
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<BookReservation> bookReservations = new ArrayList<>();
                while (resultSet.next()) {
                    BookReservation bookReservation = new BookReservation(resultSet.getInt("reservation_ID")
                            , memberDAO.find(resultSet.getInt("member_ID"))
                            , bookItemDAO.find(resultSet.getInt("barcode"))
                            , resultSet.getString("creation_date")
                            , resultSet.getString("due_date")
                            , BookReservationStatus.valueOf(resultSet.getString("status")));
                    bookReservations.add(bookReservation);
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
                    BookReservation bookReservation = new BookReservation(resultSet.getInt("reservation_ID")
                            , memberDAO.find(resultSet.getInt("member_ID"))
                            , bookItemDAO.find(resultSet.getInt("barcode"))
                            , resultSet.getString("creation_date")
                            , resultSet.getString("due_date")
                            , BookReservationStatus.valueOf(resultSet.getString("status")));
                    bookReservations.add(bookReservation);
                }
                return bookReservations;
            }
        }
    }
}
