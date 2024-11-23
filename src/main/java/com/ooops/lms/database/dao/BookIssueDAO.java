package com.ooops.lms.database.dao;

import com.mysql.cj.util.LRUCache;
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

    private static Database database;
    private static BookItemDAO bookItemDAO;
    private static MemberDAO memberDAO;

    private static LRUCache<Integer, BookIssue> bookIssueCache;

    private BookIssueDAO() {
        database = Database.getInstance();
        bookItemDAO = BookItemDAO.getInstance();
        memberDAO = MemberDAO.getInstance();
        bookIssueCache = new LRUCache<>(100);
    }

    public static synchronized BookIssueDAO getInstance() {
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
            = "Update BookIssue set member_ID = ?, barcode = ?, creation_date = ?, due_date = ?, return_date = ?, BookIssueStatus = ? where issue_ID = ?";

    // delete
    private static final String DELETE_BOOK_ISSUE
            = "Delete FROM BookIssue where issue_ID = ?";

    // find
    private static final String FIND_BOOK_ISSUE = "Select * from BookIssue where issue_ID = ?";

    // select all
    private static final String SELECT_ALL = "Select * from BookIssue";

    @Override
    public void add(@NotNull BookIssue entity) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(ADD_BOOK_ISSUE, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, entity.getMember().getPerson().getId());
            preparedStatement.setInt(2, entity.getBookItem().getBarcode());
            preparedStatement.setString(3, entity.getCreatedDate());
            preparedStatement.setString(4, entity.getDueDate());

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                entity.setIssueID(resultSet.getInt(1));
            }
            bookIssueCache.put(entity.getIssueID(), entity);
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

            if (preparedStatement.executeUpdate() > 0) {
                bookIssueCache.put(entity.getIssueID(), entity);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean delete(@NotNull BookIssue entity) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(DELETE_BOOK_ISSUE)) {
            preparedStatement.setInt(1, entity.getIssueID());

            if (preparedStatement.executeUpdate() > 0) {
                bookIssueCache.remove(entity.getIssueID());
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public BookIssue find(@NotNull Number keywords) throws SQLException {
        if (bookIssueCache.containsKey(keywords.intValue())) {
            return bookIssueCache.get(keywords.intValue());
        }
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
                            , BookIssueStatus.valueOf(resultSet.getString("BookIssueStatus")));
                    bookIssueCache.put(bookIssue.getIssueID(), bookIssue);
                    return bookIssue;
                }
            }
        }
        return null;
    }

    @Override
    public List<BookIssue> searchByCriteria(@NotNull Map<String, Object> criteria) throws SQLException {
        StringBuilder findBookIssueByCriteria = new StringBuilder("Select distinct (issue_ID) from BookIssue " +
                "join Members on BookIssue.member_ID = Members.member_ID " +
                "join BookItem on BookIssue.barcode = BookItem.barcode " +
                "join Books on Books.ISBN = BookItem.ISBN " +
                "where ");

        boolean[] flag = new boolean[15];
        int index = 1;

        for (String key : criteria.keySet()) {
            switch (key) {
                case "barcode" -> findBookIssueByCriteria.append("CAST(BookIssue.barcode AS CHAR) LIKE ? AND ");
                case "member_ID" -> findBookIssueByCriteria.append("CAST(BookIssue.member_ID AS CHAR) LIKE ? AND ");
                case "creation_date" -> {
                    flag[index] = true;
                    findBookIssueByCriteria.append("DATE(creation_date) = ? AND ");
                }
                case "due_date" -> {
                    flag[index] = true;
                    findBookIssueByCriteria.append("DATE(due_date) = ? AND ");
                }
                case "fullname" ->
                        findBookIssueByCriteria.append("CONCAT(Members.last_name, ' ', Members.first_name) LIKE ? AND ");
                default -> findBookIssueByCriteria.append(key).append(" LIKE ? AND ");
            }
            index ++;
        }

        findBookIssueByCriteria.setLength(findBookIssueByCriteria.length() - 5);

        try (PreparedStatement preparedStatement
                     = database.getConnection().prepareStatement(findBookIssueByCriteria.toString())) {
            index = 1;
            for (Object value : criteria.values()) {
                if (!flag[index]) {
                    preparedStatement.setString(index++, "%" + value.toString() + "%");
                } else {
                    preparedStatement.setString(index++, value.toString());
                }
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<BookIssue> bookIssues = new ArrayList<>();
                while (resultSet.next()) {
                    bookIssues.add(find(resultSet.getInt("issue_ID")));
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
                    bookIssues.add(find(resultSet.getInt("issue_ID")));
                }
                return bookIssues;
            }
        }
    }
}
