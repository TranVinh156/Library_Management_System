package com.ooops.lms.model;

import java.util.List;
import com.ooops.lms.model.Category;

public class Book {
    private int ISBN;
    private String title;
    private String imagePath;
    private String description;
    private String placeAt;
    private int quantity;
    private int numberOfLoanedBooks;
    private int numberOfLostBooks;
    private List<Author> authors;
    private List<Category> categories;
    private List<Comment> comments;

    public Book() {

    }

    public Book(int ISBN, String title, String description
            , String placeAt, int quantity, int numberOfLoanedBooks, int numberOfLostBooks , List<Author> authors, List<Category> categories) {
        this.ISBN = ISBN;
        this.title = title;
        this.description = description;
        this.placeAt = placeAt;
        this.quantity = quantity;
        this.numberOfLoanedBooks = numberOfLoanedBooks;
        this.numberOfLostBooks = numberOfLostBooks;
        this.authors = authors;
        this.categories = categories;
    }

    public int getISBN() {
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

    public List<Comment> getComments() {
        return comments;
    }

    public void setISBN(int ISBN) {
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

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
