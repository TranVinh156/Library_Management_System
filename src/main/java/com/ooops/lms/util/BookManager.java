package com.ooops.lms.util;

import com.ooops.lms.controller.UserMenuController;
import com.ooops.lms.database.dao.BookIssueDAO;
import com.ooops.lms.database.dao.BookMarkDAO;
import com.ooops.lms.database.dao.BookReservationDAO;
import com.ooops.lms.model.Book;
import com.ooops.lms.model.BookIssue;
import com.ooops.lms.model.BookMark;
import com.ooops.lms.model.BookReservation;

import java.sql.SQLException;
import java.util.*;

import com.ooops.lms.database.dao.BookDAO;
import com.ooops.lms.model.enums.BookIssueStatus;

public class BookManager {
    private static BookManager instance = null;
    private List<Book> allBooks = new ArrayList<>();
    private List<Book> popularBooks ;
    private List<Book> highRankBooks;
    private List<BookReservation> reservedBooks;
    private List<BookMark> markedBooks;
    private List<BookIssue> borrowedBooks;
    private List<BookIssue> borrowingBooks;


    private BookManager() {
        try {
            allBooks = BookDAO.getInstance().selectAll();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi truy vấn dữ liệu sách từ cơ sở dữ liệu", e);
        }
    }

    public List<Book> getPopularBooks() {
        if(popularBooks==null) {
            popularBooks = (ArrayList<Book>) ((ArrayList<Book>) allBooks).clone();
            sortBooks(popularBooks, (book1, book2) -> Integer.compare(
                    (book2.getNumberOfLoanedBooks() + book2.getNumberOfReservedBooks()),
                    (book1.getNumberOfLoanedBooks() + book1.getNumberOfReservedBooks())
            ));        }
        return popularBooks;
    }

    public List<Book> getHighRankBooks() {
        if(highRankBooks==null) {
            highRankBooks = (ArrayList<Book>) ((ArrayList<Book>) allBooks).clone();
            sortBooks(highRankBooks, (book1, book2) -> Integer.compare(book2.getRate(), book1.getRate()));
        }
        return highRankBooks;
    }

    public List<BookReservation> getReservedBooks() throws SQLException {
        if(reservedBooks==null) {
            Map<String, Object> criteria = new HashMap<>();
            criteria.put("member_ID", UserMenuController.getMember().getPerson().getId());
            reservedBooks = BookReservationDAO.getInstance().searchByCriteria(criteria);
        }
        return reservedBooks;
    }

    public List<BookMark> getMarkedBooks() throws SQLException {
        if(markedBooks==null) {
            markedBooks = BookMarkDAO.getInstance().findAllBookMark(UserMenuController.getMember());
        }
        return markedBooks;
    }
    public List<BookIssue> getBorrowedBooks() throws SQLException {
        if(borrowedBooks == null) {
            Map<String, Object> criteria = new HashMap<>();
            criteria.put("member_ID", UserMenuController.getMember().getPerson().getId());
            criteria.put("status", BookIssueStatus.RETURNED);
            borrowedBooks = BookIssueDAO.getInstance().searchByCriteria(criteria);
        }
        return borrowedBooks;
    }
    public List<BookIssue> getBorrowingBooks() throws SQLException {
        if(borrowingBooks == null) {
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
        if(instance == null) {
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

    private void sortBooksByPopularDescending() {
        Collections.sort(popularBooks, new Comparator<Book>() {
            @Override
            public int compare(Book book1, Book book2) {
                int rate1 = book1.getNumberOfLoanedBooks() + book1.getNumberOfReservedBooks();
                int rate2 = book2.getNumberOfLoanedBooks() + book2.getNumberOfReservedBooks();
                return Integer.compare(rate2, rate1);
            }
        });
    }

    public boolean isContainInBookMarked(BookMark bookMark ) {
        return true;
    }
}
