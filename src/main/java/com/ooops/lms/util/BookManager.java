package com.ooops.lms.util;

import com.ooops.lms.bookapi.BookInfoFetcher;
import com.ooops.lms.controller.UserMenuController;
import com.ooops.lms.database.dao.BookIssueDAO;
import com.ooops.lms.database.dao.BookMarkDAO;
import com.ooops.lms.database.dao.BookReservationDAO;
import com.ooops.lms.model.Book;
import com.ooops.lms.model.BookIssue;
import com.ooops.lms.model.BookMark;
import com.ooops.lms.model.BookReservation;

import java.io.File;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import com.ooops.lms.database.dao.BookDAO;
import com.ooops.lms.model.enums.BookIssueStatus;
import javafx.concurrent.Task;
import javafx.scene.image.Image;

public class BookManager {
    private static BookManager instance = null;
    private List<Book> allBooks = new ArrayList<>();
    private List<Book> popularBooks;
    private List<Book> highRankBooks;
    private List<BookReservation> reservedBooks;
    private List<BookMark> markedBooks;
    private List<BookIssue> borrowedBooks;
    private List<BookIssue> borrowingBooks;

    private Map<String, List<Book>> searchBooks = new HashMap<>();
    private final Map<String, Image> bookImageCache = new HashMap<>();

    private BookManager() {
        try {
            allBooks = BookDAO.getInstance().selectAll();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi truy vấn dữ liệu sách từ cơ sở dữ liệu", e);
        }
    }

    public List<Book> getPopularBooks() {
        if (popularBooks == null) {
            popularBooks = (ArrayList<Book>) ((ArrayList<Book>) allBooks).clone();
            sortBooks(popularBooks, (book1, book2) -> Integer.compare(
                    (book2.getNumberOfLoanedBooks() + book2.getNumberOfReservedBooks()),
                    (book1.getNumberOfLoanedBooks() + book1.getNumberOfReservedBooks())
            ));
        }
        return popularBooks;
    }

    public List<Book> getHighRankBooks() {
        if (highRankBooks == null) {
            highRankBooks = (ArrayList<Book>) ((ArrayList<Book>) allBooks).clone();
            sortBooks(highRankBooks, (book1, book2) -> Integer.compare(book2.getRate(), book1.getRate()));
        }
        return highRankBooks;
    }

    public List<BookReservation> getReservedBooks() throws SQLException {
        if (reservedBooks == null) {
            Map<String, Object> criteria = new HashMap<>();
            criteria.put("member_ID", UserMenuController.getMember().getPerson().getId());
            reservedBooks = BookReservationDAO.getInstance().searchByCriteria(criteria);
        }
        return reservedBooks;
    }

    public List<BookMark> getMarkedBooks() throws SQLException {
        if (markedBooks == null) {
            markedBooks = BookMarkDAO.getInstance().findAllBookMark(UserMenuController.getMember());
        }
        return markedBooks;
    }

    public List<BookIssue> getBorrowedBooks() throws SQLException {
        if (borrowedBooks == null) {
            Map<String, Object> criteria = new HashMap<>();
            criteria.put("member_ID", UserMenuController.getMember().getPerson().getId());
            criteria.put("status", BookIssueStatus.RETURNED);
            borrowedBooks = BookIssueDAO.getInstance().searchByCriteria(criteria);
        }
        return borrowedBooks;
    }

    public List<BookIssue> getBorrowingBooks() throws SQLException {
        if (borrowingBooks == null) {
            Map<String, Object> criteria = new HashMap<>();
            criteria.put("member_ID", UserMenuController.getMember().getPerson().getId());
            criteria.put("status", BookIssueStatus.BORROWED);
            borrowingBooks = BookIssueDAO.getInstance().searchByCriteria(criteria);
        }
        return borrowingBooks;
    }

    public List<Book> getAllBooks() {
        return allBooks;
    }

    public static BookManager getInstance() {
        if (instance == null) {
            instance = new BookManager();
        }
        return instance;
    }

    private void sortBooks(List<Book> books, Comparator<Book> comparator) {
        Collections.sort(books, comparator);
    }

    private void sortBooksByRateDescending() {
        Collections.sort(highRankBooks, new Comparator<Book>() {
            @Override
            public int compare(Book book1, Book book2) {
                int rate1 = book1.getRate();
                int rate2 = book2.getRate();
                return Integer.compare(rate2, rate1);
            }
        });
    }

    public Image getBookImage(String url) {
        // Kiểm tra trong cache
        if (bookImageCache.containsKey(url)) {
            return bookImageCache.get(url); // Lấy từ cache
        }

        // Nếu không có, tải ảnh và lưu vào cache
        Image image = new Image(url, true); // `true` để tải ảnh không đồng bộ
        bookImageCache.put(url, image);
        return image;
    }

    public Book isContainInAllBooks(Book book) {
        Book findBook;
        try {
            findBook = BookDAO.getInstance().find(book.getISBN());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return findBook;
    }

    public int getReservedBookSize() {
        try {
            getReservedBooks();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reservedBooks.size();
    }

    public int getBorrowingBookSize() {
        try {
            getBorrowingBooks();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return borrowingBooks.size();
    }

    public void addReservedBook(BookReservation bookReservation) {
        try {
            getReservedBooks();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        reservedBooks.add(bookReservation);
    }

    public List<Book> searchBookByAPI(String searchText) {
        // Tạo Task để tìm kiếm sách bất đồng bộ
        Task<List<Book>> fetchBooksTask = new Task<>() {
            @Override
            protected List<Book> call() throws Exception {
                // Thực hiện tìm kiếm sách trên một luồng riêng
                return BookInfoFetcher.searchBooksByKeyword(searchText);
            }
        };
        AtomicReference<List<Book>> bookList = new AtomicReference<>(new ArrayList<>());
        fetchBooksTask.setOnSucceeded(event -> {
            bookList.set(fetchBooksTask.getValue());
        });
        return bookList.get();
    }


    public Task<Image> createLoadImageTask(Book book) {
        return new Task<>() {
            @Override
            protected Image call() throws Exception {
                String imagePath = book.getImagePath();

                // Kiểm tra ảnh trong cache
                if (bookImageCache.containsKey(imagePath)) {
                    return bookImageCache.get(imagePath);
                }

                try {
                    // Tải ảnh mới
                    Image image = new Image(imagePath, true);
                    bookImageCache.put(imagePath, image); // Lưu vào cache
                    return image;
                } catch (Exception e) {
                    System.out.println("Failed to load image. Path length: " + imagePath.length());

                    // Tải ảnh mặc định
                    File file = new File("bookImage/default.png");
                    Image defaultImage = new Image(file.toURI().toString());
                    bookImageCache.put(imagePath, defaultImage); // Lưu ảnh mặc định vào cache với cùng key
                    return defaultImage;
                }
            }
        };
    }

    public void clearCache() {
        bookImageCache.clear(); // Làm sạch cache khi cần thiết
    }


    public Map<String, List<Book>> getBookSearchCache() {
        return searchBooks;
    }

    public void addBookSearchCache(String key, List<Book> books) {
        searchBooks.put(key, books);
    }
}
