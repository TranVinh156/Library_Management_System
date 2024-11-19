package com.ooops.lms.database.dao;

import com.ooops.lms.database.Database;
import com.ooops.lms.model.BookMark;
import com.ooops.lms.model.Member;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookMarkDAO implements DatabaseQuery<BookMark> {
    private static BookMarkDAO bookMarkDAO;

    private Database database;
    private MemberDAO memberDAO;
    private BookDAO bookDAO;

    private BookMarkDAO() {
        database = Database.getInstance();
        memberDAO = MemberDAO.getInstance();
        bookDAO = BookDAO.getInstance();
    }

    public static BookMarkDAO getInstance() {
        if (bookMarkDAO == null) {
            bookMarkDAO = new BookMarkDAO();
        }
        return bookMarkDAO;
    }

    // add
    private static final String ADD_BOOK_MARK = "INSERT INTO bookmark (member_id, ISBN) VALUES (?, ?)";

    // delete
    private static final String DELETE_BOOK_MARK = "DELETE FROM bookmark WHERE member_ID = ? and ISBN = ?";

    // select all
    private static final String FIND_BOOK_MARK = "SELECT * FROM bookmark where member_id = ?";

    @Override
    public void add(@NotNull BookMark entity) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(ADD_BOOK_MARK)) {
            preparedStatement.setInt(1, entity.getMember().getPerson().getId());
            preparedStatement.setLong(2, entity.getBook().getISBN());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public boolean update(BookMark entity) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(@NotNull BookMark entity) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(DELETE_BOOK_MARK)) {
            preparedStatement.setInt(1, entity.getMember().getPerson().getId());
            preparedStatement.setLong(2, entity.getBook().getISBN());

            return preparedStatement.executeUpdate() > 0;
        }
    }

    @Override
    public BookMark find(Number keywords) throws SQLException {

        return null;
    }

    @Override
    public List<BookMark> searchByCriteria(Map<String, Object> criteria) throws SQLException {
        return List.of();
    }

    @Override
    public List<BookMark> selectAll() throws SQLException {
        return List.of();
    }

    public List<BookMark> findAllBookMark(@NotNull Member member) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(FIND_BOOK_MARK)) {
            preparedStatement.setInt(1, member.getPerson().getId());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<BookMark> bookMarks = new ArrayList<>();
                while (resultSet.next()) {
                    BookMark bookMark = new BookMark(member, bookDAO.find(resultSet.getLong("ISBN")));
                    bookMarks.add(bookMark);
                }
                return bookMarks;
            }
        }
    }
}
