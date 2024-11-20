package com.ooops.lms.database.dao;

import com.mysql.cj.util.LRUCache;
import com.ooops.lms.database.Database;
import com.ooops.lms.model.Author;
import com.ooops.lms.model.Book;
import com.ooops.lms.model.Category;
import com.ooops.lms.model.enums.BookStatus;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookDAO implements DatabaseQuery<Book> {
    private Database database;
    private static BookDAO bookDAO;

    private BookDAO() {
        database = Database.getInstance();
    }

    public static BookDAO getInstance() {
        if (bookDAO == null) {
            bookDAO = new BookDAO();
        }
        return bookDAO;
    }

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
    private static final String FIND_BOOK_AUTHOR
            = "Select * from Authors a join Books_Authors b_a on a.author_ID = b_a.author_ID where b_a.ISBN = ?";
    private static final String FIND_BOOK_CATEGORY
            = "Select * from Category c join Books_Category b_c on c.category_ID = b_c.category_ID where ISBN = ?";

    //
    private static final String SELECT_ALL = "Select * from Books";
    private static final String SELECT_ALL_CATEGORY = "Select * from Category";
    private static final String SELECT_ALL_AUTHOR = "Select * from Authors";

    /**
     * Thêm book vào csdl.
     *
     * @param book book
     * @throws SQLException
     */
    private void insertBook(@NotNull Book book) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(INSERT_NEW_BOOK)) {
            preparedStatement.setLong(1, book.getISBN());
            preparedStatement.setString(2, book.getImagePath());
            preparedStatement.setString(3, book.getTitle());
            preparedStatement.setString(4, book.getDescription());
            preparedStatement.setString(5, book.getPlaceAt());
            preparedStatement.executeUpdate();
        }
    }

    /**
     * Chèn tac giả vào csdl.
     *
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
     *
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
     *
     * @param ISBN
     * @param authorID
     * @throws SQLException
     */
    private void linkBookAuthor(long ISBN, int authorID) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(INSERT_NEW_BOOK_AUTHOR)) {
            preparedStatement.setLong(1, ISBN);
            preparedStatement.setInt(2, authorID);
            preparedStatement.executeUpdate();
        }
    }

    /**
     * Thêm category vào csdl.
     *
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
     *
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
     *
     * @param ISBN       ISBN
     * @param categoryID category id
     * @throws SQLException
     */
    private void linkBookCategory(long ISBN, int categoryID) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(INSERT_NEW_BOOK_CATEGORY)) {
            preparedStatement.setLong(1, ISBN);
            preparedStatement.setInt(2, categoryID);
            preparedStatement.executeUpdate();
        }
    }

    /**
     * Thêm các sách bản sao.
     *
     * @param ISBN
     * @param quantity số lượng bản sao.
     * @throws SQLException
     */
    private void insertBookItem(long ISBN, String placeAt, int quantity) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(INSERT_NEW_BOOKITEM)) {
            for (int i = 0; i < quantity; i++) {
                preparedStatement.setLong(1, ISBN);
                preparedStatement.executeUpdate();
            }
        }
    }

    /*
    public void addNewBook(@NotNull Book book) throws SQLException {

    }
     */

    /**
     * Thêm sách mới.
     *
     * @param entity new book
     * @throws SQLException
     */
    @Override
    public void add(@NotNull Book entity) throws SQLException {
        if (find(entity.getISBN()) != null) {
            throw new SQLException("Book is exsit");
        }

        database.getConnection().setAutoCommit(false);

        try {
            insertBook(entity);

            for (Author author : entity.getAuthors()) {
                int authorID = findOrCreateAuthor(author);
                linkBookAuthor(entity.getISBN(), authorID);
            }

            for (Category category : entity.getCategories()) {
                int categoryID = findOrCreateCategory(category);
                linkBookCategory(entity.getISBN(), categoryID);
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

    /**
     * Cập nhập sách.
     *
     * @param entity sách sau khi sửa thông tin
     * @return True nếu thành công và ngược lại
     * @throws SQLException
     */
    @Override
    public boolean update(@NotNull Book entity) throws SQLException {
        try {
            database.getConnection().setAutoCommit(false);

            long isbn = entity.getISBN();

            // xoá liên kết trong Books_Author
            try (PreparedStatement stmt = database.getConnection().prepareStatement(DELETE_BOOK_AUTHORS)) {
                stmt.setLong(1, isbn);
                stmt.executeUpdate();
            }

            // Xóa liên kết trong Books_Category
            try (PreparedStatement stmt = database.getConnection().prepareStatement(DELETE_BOOK_CATEGORY)) {
                stmt.setLong(1, isbn);
                stmt.executeUpdate();
            }

            try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(UPDATE_BOOK)) {
                preparedStatement.setString(1, entity.getTitle());
                preparedStatement.setString(2, entity.getImagePath());
                preparedStatement.setString(3, entity.getDescription());
                preparedStatement.setString(4, entity.getPlaceAt());
                preparedStatement.setLong(5, entity.getISBN());
                preparedStatement.executeUpdate();

                database.getConnection().commit();
                return true;
            }

        } catch (SQLException e) {
            database.getConnection().rollback();
            throw e;
        } finally {
            database.getConnection().setAutoCommit(true);
        }

    }

    @Override
    public boolean delete(@NotNull Book entity) throws SQLException {
        if (find(entity.getISBN()) == null) {
            throw new SQLException("Book does not exist");
        }
        try {
            database.getConnection().setAutoCommit(false);

            long isbn = entity.getISBN();
            // Xóa liên kết trong Books_Authors
            try (PreparedStatement stmt = database.getConnection().prepareStatement(DELETE_BOOK_AUTHORS)) {
                stmt.setLong(1, isbn);
                stmt.executeUpdate();
            }

            // Xóa liên kết trong Books_Category
            try (PreparedStatement stmt = database.getConnection().prepareStatement(DELETE_BOOK_CATEGORY)) {
                stmt.setLong(1, isbn);
                stmt.executeUpdate();
            }

            // Xóa các bản ghi trong BookItem
            try (PreparedStatement stmt = database.getConnection().prepareStatement(DELETE_BOOK_ITEMS)) {
                stmt.setLong(1, isbn);
                stmt.executeUpdate();
            }

            // Xóa sách trong Books
            try (PreparedStatement stmt = database.getConnection().prepareStatement(DELETE_BOOK)) {
                stmt.setLong(1, isbn);
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

    /**
     * Tìm book theo mã.
     *
     * @param keywords mã
     * @return sách
     * @throws SQLException
     */
    @Override
    public Book find(Number keywords) throws SQLException {
        List<Author> authorList = new ArrayList<>();
        List<Category> categoryList = new ArrayList<>();
        List<Book> bookList = new ArrayList<>();

        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(FIND_BOOK_AUTHOR)) {
            preparedStatement.setLong(1, (Long) keywords);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                authorList.add(new Author(resultSet.getInt("author_ID"), resultSet.getString("author_name")));
            }
        }

        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(FIND_BOOK_CATEGORY)) {
            preparedStatement.setLong(1, (Long) keywords);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                categoryList.add(new Category(resultSet.getInt("category_ID"), resultSet.getString("category_name")));
            }
        }

        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(FIND_BOOK)) {
            preparedStatement.setLong(1, (Long) keywords);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Book book = new Book(resultSet.getLong("ISBN")
                            , resultSet.getString("title")
                            , resultSet.getString("image_path")
                            , resultSet.getString("description")
                            , resultSet.getString("placeAt"), authorList, categoryList);
                    book.setQuantity(resultSet.getInt("quantity"));
                    book.setNumberOfLoanedBooks(resultSet.getInt("number_loaned_book"));
                    book.setNumberOfLostBooks(resultSet.getInt("number_lost_book"));
                    book.setNumberOfReservedBooks(resultSet.getInt("number_reserved_book"));
                    book.setRate(resultSet.getInt("rate"));
                    book.setStatus(BookStatus.valueOf(resultSet.getString("book_status")));
                    bookList.add(book);

                    // Lưu vào bộ nhớ đệm
                    return book;
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Tìm kiếm theo tiêu chí.
     *
     * @param criteria danh sách tiêu chí
     * @return danh sách sách
     * @throws SQLException
     */
    @Override
    public List<Book> searchByCriteria(@NotNull Map<String, Object> criteria) throws SQLException {

        List<Book> bookList = new ArrayList<>();
        StringBuilder findBookByCriteria = new StringBuilder("SELECT distinct (Books.ISBN)\n" +
                "FROM Books\n" +
                "JOIN Books_Authors ON Books.ISBN = Books_Authors.ISBN\n" +
                "JOIN Authors ON Books_Authors.author_ID = Authors.author_ID\n" +
                "JOIN Books_Category ON Books.ISBN = Books_Category.ISBN\n" +
                "JOIN Category ON Books_Category.category_ID = Category.category_ID\n" +
                " WHERE ");

        for (String key : criteria.keySet()) {
            if (key.equals("ISBN")) {
                findBookByCriteria.append("CAST(Books.ISBN AS CHAR) LIKE ? AND ");
            } else {
                findBookByCriteria.append(key).append(" LIKE ? AND ");
            }
        }
        findBookByCriteria.setLength(findBookByCriteria.length() - 5);

        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(findBookByCriteria.toString())) {
            int index = 1;

            for (Object value : criteria.values()) {
                System.out.println(value);
                preparedStatement.setString(index++, "%" + value + "%");
            }

            System.out.println(preparedStatement.toString());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    bookList.add(find(resultSet.getLong("Books.ISBN")));
                }
                return bookList;
            }
        }

    }

    /**
     * Lấy tất cả sách.
     *
     * @return danh sách book
     * @throws SQLException
     */
    @Override
    public List<Book> selectAll() throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(SELECT_ALL)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Book> bookList = new ArrayList<>();
                while (resultSet.next()) {
                    bookList.add(find(resultSet.getLong("ISBN")));
                }

                return bookList;
            }
        }
    }

    /**
     * danh sách tiêu chí.
     *
     * @param criteria danh sách tiêu chí
     * @return keywords
     */
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

    public List<Category> selectAllCategory() throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(SELECT_ALL_CATEGORY)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Category> categoryList = new ArrayList<>();
                while (resultSet.next()) {
                    categoryList.add(new Category(resultSet.getInt("category_ID"), resultSet.getString("category_name")));
                }
                return categoryList;
            }
        }
    }

    public List<Author> selectAllAuthor() throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(SELECT_ALL_AUTHOR)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Author> authorList = new ArrayList<>();
                while (resultSet.next()) {
                    authorList.add(new Author(resultSet.getInt("author_ID"), resultSet.getString("author_name")));
                }
                return authorList;
            }
        }
    }

}
