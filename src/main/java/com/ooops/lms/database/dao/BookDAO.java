package com.ooops.lms.database.dao;

import com.ooops.lms.database.Database;
import com.ooops.lms.model.Author;
import com.ooops.lms.model.Book;
import com.ooops.lms.model.Category;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class BookDAO implements DatabaseQuery<Book> {
    private Database database = Database.getInstance();

    private static final String SELECT_AUTHOR = "Select * from Authors where author_name = ?";

    private static final String INSERT_NEW_AUTHOR = "Insert into Authors (author_name) values (?)";

    private static final String SELECT_CATEGORY = "Select * from Category where category_name = ?";

    private static final String INSERT_NEW_CATEGORY = "Insert into Category (category_name) values (?)";

    private static final String INSERT_NEW_BOOK_AUTHOR
            = "Insert into Books_Authors(ISBN, author_ID) values (?, ?)";

    private static final String INSERT_NEW_BOOK_CATEGORY
            = "Insert into Books_Category(ISBN, category_ID) values (?, ?)";

    private static final String INSERT_NEW_BOOKITEM = "Insert into BookItem(ISBN) values (?)";

    private static final String INSERT_NEW_BOOK
            = "Insert into Books (ISBN, image_path, title, description, placeAt) values (?, ?, ?, ?, ?)";

    private static final String SELECT_BOOK_BY_ISBN = "Select * from Books where ISBN = ?";

    // update Book
    private static final String UPDATE_BOOK
            = "Update Books set title = ?, image_path = ?, description = ?, placeAt = ? where ISBN = ?";

    // delete Book
    private static final String DELETE_BOOK = "DELETE FROM Books WHERE ISBN = ?";

    private static final String DELETE_BOOK_AUTHORS = "DELETE FROM Books_Authors WHERE ISBN = ?";

    private static final String DELETE_BOOK_CATEGORY = "DELETE FROM Books_Category WHERE ISBN = ?";

    private static final String DELETE_BOOK_ITEMS = "DELETE FROM BookItem WHERE ISBN = ?";

    // search
    private static final String FIND_BOOK = "Select * from Books where ISBN = ?";

    /**
     * Thêm book vào csdl.
     *
     * @param book book
     * @throws SQLException
     */
    private void insertBook(Book book) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(INSERT_NEW_BOOK)) {
            preparedStatement.setInt(1, book.getISBN());
            preparedStatement.setString(2, book.getImagePath());
            preparedStatement.setString(3, book.getTitle());
            preparedStatement.setString(4, book.getDescription());
            preparedStatement.setString(5, book.getPlaceAt());
            preparedStatement.executeUpdate();
        }
    }

    /**
     * Chèn tac giả vào csdl.
     * @param author
     * @return trả về id tác giả
     * @throws SQLException
     */
    private int insertAuthor(@NotNull Author author) throws SQLException {
        try (PreparedStatement preparedStatement
                     = database.getConnection().prepareStatement(INSERT_NEW_AUTHOR, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, author.getAuthorName());
            preparedStatement.executeUpdate();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                } else {
                    throw new SQLException("Tạo tác giả lỗi");
                }
            }
        }
    }

    /**
     * Kiểm tra hoặc tạo tác giả.
     * @param author
     * @return id tác giả
     * @throws SQLException
     */
    private int findOrCreateAuthor(@NotNull Author author) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(SELECT_AUTHOR)) {
            preparedStatement.setString(1, author.getAuthorName());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("author_ID");
                } else {
                    return insertAuthor(author);
                }
            }
        }
    }

    /**
     * Liên kết sách và tác giả.
     * @param ISBN
     * @param authorID
     * @throws SQLException
     */
    private void linkBookAuthor(int ISBN, int authorID) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(INSERT_NEW_BOOK_AUTHOR)) {
            preparedStatement.setInt(1, ISBN);
            preparedStatement.setInt(2, authorID);
            preparedStatement.executeUpdate();
        }
    }

    /**
     * Thêm category vào csdl.
     * @param category thể loại
     * @return category id
     * @throws SQLException
     */
    private int insertCategory(@NotNull Category category) throws SQLException {
        try (PreparedStatement preparedStatement
                     = database.getConnection().prepareStatement(INSERT_NEW_CATEGORY, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, category.getCatagoryName());
            preparedStatement.executeUpdate();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                } else {
                    throw new SQLException("Thêm category lỗi");
                }
            }
        }
    }

    /**
     * Kiểm tra thể loại có trong csdl không.
     * @param category thể loại
     * @return category id
     * @throws SQLException
     */
    private int findOrCreateCategory(@NotNull Category category) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(SELECT_CATEGORY)) {
            preparedStatement.setString(1, category.getCatagoryName());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("category_ID");
                } else {
                    return insertCategory(category);
                }
            }
        }
    }

    /**
     * Liên kết sách với thể loại.
     * @param ISBN ISBN
     * @param categoryID category id
     * @throws SQLException
     */
    private void linkBookCategory(int ISBN, int categoryID) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(INSERT_NEW_BOOK_CATEGORY)) {
            preparedStatement.setInt(1, ISBN);
            preparedStatement.setInt(2, categoryID);
            preparedStatement.executeUpdate();
        }
    }

    /**
     * Thêm các sách bản sao.
     * @param ISBN
     * @param quanlity số lượng bản sao.
     * @throws SQLException
     */
    private void insertBookItem(int ISBN, String placeAt, int quanlity) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(INSERT_NEW_BOOKITEM)) {
            for (int i = 0; i < quanlity; i++) {
                preparedStatement.setInt(1, ISBN);
                preparedStatement.executeUpdate();
            }
        }
    }

    public void addNewBook(@NotNull Book book) throws SQLException {

    }

    @Override
    public void add(Book entity) throws SQLException {
        database.getConnection().setAutoCommit(false);

        try {
            insertBook(entity);

            for (Author author : entity.getAuthors()) {
                int authorID = findOrCreateAuthor(author);
                linkBookAuthor(authorID, entity.getISBN());
            }

            for (Category category : entity.getCategories()) {
                int categoryID = findOrCreateCategory(category);
                linkBookCategory(categoryID, entity.getISBN());
            }

            insertBookItem(entity.getISBN(), entity.getPlaceAt(), entity.getQuantity());

            database.getConnection().commit();
        } catch (SQLException e) {
            database.getConnection().rollback();
            throw e;
        } finally {
            database.getConnection().setAutoCommit(true);
        }
    }

    @Override
    public boolean update(Book entity) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(UPDATE_BOOK)) {
            preparedStatement.setString(1, entity.getTitle());
            preparedStatement.setString(2, entity.getImagePath());
            preparedStatement.setString(3, entity.getDescription());
            preparedStatement.setString(4, entity.getPlaceAt());
            preparedStatement.setInt(5, entity.getISBN());

            return preparedStatement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Book entity) throws SQLException {
        try {
            database.getConnection().setAutoCommit(false);

            int isbn = entity.getISBN();
            // Xóa liên kết trong Books_Authors
            try (PreparedStatement stmt = database.getConnection().prepareStatement(DELETE_BOOK_AUTHORS)) {
                stmt.setInt(1, isbn);
                stmt.executeUpdate();
            }

            // Xóa liên kết trong Books_Category
            try (PreparedStatement stmt = database.getConnection().prepareStatement(DELETE_BOOK_CATEGORY)) {
                stmt.setInt(1, isbn);
                stmt.executeUpdate();
            }

            // Xóa các bản ghi trong BookItem
            try (PreparedStatement stmt = database.getConnection().prepareStatement(DELETE_BOOK_ITEMS)) {
                stmt.setInt(1, isbn);
                stmt.executeUpdate();
            }

            // Xóa sách trong Books
            try (PreparedStatement stmt = database.getConnection().prepareStatement(DELETE_BOOK)) {
                stmt.setInt(1, isbn);
                int rowsDeleted = stmt.executeUpdate();
                database.getConnection().commit();
                return rowsDeleted > 0;
            }
        } catch (SQLException e) {
            database.getConnection().rollback();
            throw e;
        } finally {
            database.getConnection().setAutoCommit(true);
        }
    }

    @Override
    public Book find(String keywords) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(FIND_BOOK)) {
            preparedStatement.setString(1, keywords);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    //Book book = new Book(resultSet.getInt("ISBN"), );

                } else {
                    throw new SQLException("No book found");
                }
            }
        }
        return null;
    }

    @Override
    public List<Book> searchByCriteria(Map<String, Object> criteria) throws SQLException {
        return List.of();
    }

}
