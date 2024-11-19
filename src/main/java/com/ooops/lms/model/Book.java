package com.ooops.lms.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ooops.lms.database.dao.CommentDAO;
import com.ooops.lms.model.Category;

public class Book {
    public static final String DEFAULT_IMAGE_PATH = "Library_Management_System/bookImage/default.png";
    private long ISBN;
    private String title;
    private String imagePath;
    private String description;
    private String placeAt;
    private int quantity;
    private int numberOfLoanedBooks;
    private int numberOfLostBooks;
    private int numberOfReservedBooks;
    private int rate;
    private List<Author> authors;
    private List<Category> categories;

    public Book() {
        authors = new ArrayList<Author>();
        categories = new ArrayList<>();
    }

    public Book(Book book) {
        this.ISBN = book.getISBN();
        this.title = book.getTitle();
        this.imagePath = book.getImagePath();
        this.description = book.getDescription();
        this.placeAt = book.getPlaceAt();
        this.quantity = book.getQuantity();
        this.numberOfLoanedBooks = book.getNumberOfLoanedBooks();
        this.numberOfLostBooks = book.getNumberOfLostBooks();
        this.authors = book.getAuthors();
        this.categories = book.getCategories();
    }

    // có image_path khi nhập sách
    public Book(long ISBN, String title, String imagePath, String description
            , String placeAt, List<Author> authors, List<Category> categories, int quantity) {
        this.ISBN = ISBN;
        this.title = title;
        this.description = description;
        this.imagePath = imagePath;
        this.placeAt = placeAt;
        this.authors = authors;
        this.categories = categories;
        this.quantity = quantity;
    }

    // Không có image_path khi nhập sách
    public Book(long ISBN, String title, String description
            , String placeAt, List<Author> authors, List<Category> categories, int quantity) {
        this.ISBN = ISBN;
        this.title = title;
        this.description = description;
        this.imagePath = DEFAULT_IMAGE_PATH;
        this.placeAt = placeAt;
        this.authors = authors;
        this.categories = categories;
        this.quantity = quantity;
    }

    // có image_path
    public Book(long ISBN, String title, String imagePath, String description
            , String placeAt, List<Author> authors, List<Category> categories) {
        this.ISBN = ISBN;
        this.title = title;
        this.description = description;
        this.imagePath = imagePath;
        this.placeAt = placeAt;
        this.numberOfLoanedBooks = 0;
        this.numberOfLostBooks = 0;
        this.authors = authors;
        this.categories = categories;
    }

    // Không có image_path
    public Book(long ISBN, String title, String description
            , String placeAt, List<Author> authors, List<Category> categories) {
        this.ISBN = ISBN;
        this.title = title;
        this.description = description;
        this.imagePath = DEFAULT_IMAGE_PATH;
        this.placeAt = placeAt;
        this.numberOfLoanedBooks = 0;
        this.numberOfLostBooks = 0;
        this.authors = authors;
        this.categories = categories;
    }
    @Override
    public boolean equals(Object o) {
        if(o instanceof Book) {
            Book book = (Book) o;
            return this.ISBN == book.getISBN();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(ISBN);
    }

    public long getISBN() {
        return ISBN;
    }

    public String getTitle() {
        return title;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getDescription() {
        return description;
    }

    public String getPlaceAt() {
        return placeAt;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getNumberOfLoanedBooks() {
        return numberOfLoanedBooks;
    }

    public int getNumberOfLostBooks() {
        return numberOfLostBooks;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public int getNumberOfReservedBooks() {
        return numberOfReservedBooks;
    }

    public int getRate() {
        return rate;
    }

    public void setISBN(long ISBN) {
        this.ISBN = ISBN;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPlaceAt(String placeAt) {
        this.placeAt = placeAt;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setNumberOfLoanedBooks(int numberOfLoanedBooks) {
        this.numberOfLoanedBooks = numberOfLoanedBooks;
    }

    public void setNumberOfLostBooks(int numberOfLostBooks) {
        this.numberOfLostBooks = numberOfLostBooks;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public void setNumberOfReservedBooks(int numberOfReservedBooks) {
        this.numberOfReservedBooks = numberOfReservedBooks;
    }
}
