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

    private static final LRUCache<String, Image> bookImageCache = new LRUCache<>(70);

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
        reservedBooks.add(bookReservation);
    }
    public void deleteReservedBook(BookReservation bookReservation) {
        int index = findBookReserved(bookReservation.getBookItem().getBarcode());

        if(index!=-1) {
            reservedBooks.remove(index);
        }
    }

    private int findBookReserved(long barCode) {
        try {
            reservedBooks = BookManager.getInstance().getReservedBooks();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0;i<reservedBooks.size();i++) {
            if (reservedBooks.get(i).getBookItem().getBarcode() == barCode) {
                return i;
            }
        }
        return -1;
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
        if (index != -1) {
            markedBooks.remove(index);
        }
    }

    private int findBookMark(long ISBN) {
        for (int i = 0; i < markedBooks.size(); i++) {
            if (markedBooks.get(i).getBook().getISBN() == ISBN) {
                return i;
            }
        }
        return -1;
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
