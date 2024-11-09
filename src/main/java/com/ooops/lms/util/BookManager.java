package com.ooops.lms.util;

import com.ooops.lms.model.Book;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.ooops.lms.database.dao.BookDAO;

public class BookManager {
    private static BookManager instance = null;
    private List<Book> allBooks = new ArrayList<>();
    private List<Book> popularBooks ;
    private List<Book> highRankBooks;

    private BookDAO bookDAO = new BookDAO();

    private BookManager() {
        try {
            allBooks = bookDAO.selectAll();
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
}
