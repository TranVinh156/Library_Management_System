package com.ooops.lms.database.dao;

import com.ooops.lms.database.Database;
import com.ooops.lms.model.BookIssue;
import com.ooops.lms.model.BookItem;
import com.ooops.lms.model.enums.BookIssueStatus;
import com.ooops.lms.model.enums.BookItemStatus;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BookIssueDAO implements DatabaseQuery<BookIssue> {
    private static BookIssueDAO bookIssueDAO;

    private Database database;
    private BookItemDAO bookItemDAO;
    private MemberDAO memberDAO;

    private BookIssueDAO() {
        database = Database.getInstance();
        bookItemDAO = BookItemDAO.getInstance();
        memberDAO = MemberDAO.getInstance();
    }

    public static BookIssueDAO getInstance() {
        if (bookIssueDAO == null) {
            bookIssueDAO = new BookIssueDAO();
        }
        return bookIssueDAO;
    }

    // add
    private static final String ADD_BOOK_ISSUE
            = "Insert into BookIssue(member_ID, barcode, creation_date, due_date) values (?, ?, ?, ?)";

    // update
    private static final String UPDATE_BOOK_ISSUE
            = "Update BookIssue set member_ID = ?, barcode = ?, creation_date = ?, due_date = ?, return_date = ?, status = ? where issue_ID = ?";

    // delete
    private static final String DELETE_BOOK_ISSUE
            = "Delete BookIssue where issue_ID = ?";

    // find
    private static final String FIND_BOOK_ISSUE = "Select * from BookIssue where issue_ID = ?";

    // select all
    private static final String SELECT_ALL = "Select * from BookIssue";

    @Override
    public void add(@NotNull BookIssue entity) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(ADD_BOOK_ISSUE)) {
            preparedStatement.setInt(1, entity.getMember().getPerson().getId());
            preparedStatement.setInt(2, entity.getBookItem().getBarcode());
            preparedStatement.setString(3, entity.getCreatedDate());
            preparedStatement.setString(4, entity.getDueDate());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public boolean update(@NotNull BookIssue entity) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(UPDATE_BOOK_ISSUE)) {
            preparedStatement.setInt(1, entity.getMember().getPerson().getId());
            preparedStatement.setInt(2, entity.getBookItem().getBarcode());
            preparedStatement.setString(3, entity.getCreatedDate());
            preparedStatement.setString(4, entity.getDueDate());
            preparedStatement.setString(5, entity.getReturnDate());
            preparedStatement.setString(6, entity.getStatus().name());
            preparedStatement.setInt(7, entity.getIssueID());

            return preparedStatement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(@NotNull BookIssue entity) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(DELETE_BOOK_ISSUE)) {
            preparedStatement.setInt(1, entity.getIssueID());
            return preparedStatement.executeUpdate() > 0;
        }
    }

    @Override
    public BookIssue find(@NotNull Number keywords) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(FIND_BOOK_ISSUE)) {
            preparedStatement.setInt(1, keywords.intValue());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    BookIssue bookIssue = new BookIssue(resultSet.getInt("issue_ID")
                            , memberDAO.find(resultSet.getInt("member_ID"))
                            , bookItemDAO.find(resultSet.getInt("barcode"))
                            , resultSet.getString("creation_date")
                            , resultSet.getString("due_date")
                            , resultSet.getString("return_date")
                            , BookIssueStatus.valueOf(resultSet.getString("status")));
                    return bookIssue;
                }
            }
        }
        return null;
    }

    @Override
    public List<BookIssue> searchByCriteria(@NotNull Map<String, Object> criteria) throws SQLException {
        StringBuilder findBookIssueByCriteria = new StringBuilder("Select * from BookIssue where ");
        Iterator<String> iterator = criteria.keySet().iterator();

        while (iterator.hasNext()) {
            String key = iterator.next();
            findBookIssueByCriteria.append(key).append(" LIKE ?");
            if (iterator.hasNext()) {
                findBookIssueByCriteria.append(" AND ");
            }
        }

        findBookIssueByCriteria.setLength(findBookIssueByCriteria.length() - 5);

        try (PreparedStatement preparedStatement
                     = database.getConnection().prepareStatement(findBookIssueByCriteria.toString())) {
            int index = 1;
            for (Object value : criteria.values()) {
                preparedStatement.setString(index++, "%" + value.toString() + "%");
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<BookIssue> bookIssues = new ArrayList<>();
                while (resultSet.next()) {
                    BookIssue bookIssue = new BookIssue(resultSet.getInt("issue_ID")
                            , memberDAO.find(resultSet.getInt("member_ID"))
                            , bookItemDAO.find(resultSet.getInt("barcode"))
                            , resultSet.getString("creation_date")
                            , resultSet.getString("due_date")
                            , resultSet.getString("return_date")
                            , BookIssueStatus.valueOf(resultSet.getString("status")));
                    bookIssues.add(bookIssue);
                }
                return bookIssues;
            }
        }
    }

    @Override
    public List<BookIssue> selectAll() throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(SELECT_ALL)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<BookIssue> bookIssues = new ArrayList<>();
                while (resultSet.next()) {
                    BookIssue bookIssue = new BookIssue(resultSet.getInt("issue_ID")
                            , memberDAO.find(resultSet.getInt("member_ID"))
                            , bookItemDAO.find(resultSet.getInt("barcode"))
                            , resultSet.getString("creation_date")
                            , resultSet.getString("due_date")
                            , resultSet.getString("return_date")
                            , BookIssueStatus.valueOf(resultSet.getString("status")));
                    bookIssues.add(bookIssue);
                }
                return bookIssues;
            }
        }
    }
}
