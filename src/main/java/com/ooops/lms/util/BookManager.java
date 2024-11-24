package com.ooops.lms.util;

import com.mysql.cj.util.LRUCache;
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

    private static final LRUCache<String, Image> bookImageCache = new LRUCache<>(50);

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
            criteria.put("bookIssueStatus", BookIssueStatus.RETURNED);
            borrowedBooks = BookIssueDAO.getInstance().searchByCriteria(criteria);
        }
        return borrowedBooks;
    }

    public List<BookIssue> getBorrowingBooks() throws SQLException {
        if (borrowingBooks == null) {
            Map<String, Object> criteria = new HashMap<>();
            criteria.put("member_ID", UserMenuController.getMember().getPerson().getId());
            criteria.put("bookIssueStatus", BookIssueStatus.BORROWED);
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
    public void addMarkedBook(BookMark bookMark) {
        try {
            getMarkedBooks();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        markedBooks.add(bookMark);
    }

    public void deleteMarkedBook(BookMark bookMark) {
        try {
            getMarkedBooks();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int index = findBookMark(bookMark.getBook().getISBN());
        if(index!=-1) {
            markedBooks.remove(index);
        }
    }
    private int findBookMark(long ISBN) {
        for (int i = 0;i<markedBooks.size();i++) {
            if (markedBooks.get(i).getBook().getISBN() == ISBN) {
                return i;
            }
        }
        return -1;
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
        instance = null;
        bookImageCache.clear();
        allBooks.clear();
        popularBooks.clear();
        highRankBooks.clear();
        if (reservedBooks != null) {
            reservedBooks.clear();
        }
        if (markedBooks != null) {
            markedBooks.clear();
        }
        if (borrowedBooks != null) {
            borrowedBooks.clear();
        }
        if (borrowingBooks != null) {
            borrowingBooks.clear();
        }
    }

    public boolean isContainedInMarkedBookList(Long ISBN) {
        try {
            getMarkedBooks();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (BookMark bookMark : markedBooks) {
            if (bookMark.getBook().getISBN() == ISBN) {
                return true;
            }
        }
        return false;
    }

}
