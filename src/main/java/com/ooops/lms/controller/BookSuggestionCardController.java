package com.ooops.lms.controller;

import com.ooops.lms.database.dao.BookDAO;
import com.ooops.lms.model.Author;
import com.ooops.lms.model.Book;
import com.ooops.lms.model.BookMark;
import com.ooops.lms.util.BookManager;
import com.ooops.lms.util.FXMLLoaderUtil;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class BookSuggestionCardController {
    private FXMLLoaderUtil fxmlLoaderUtil = FXMLLoaderUtil.getInstance();

    private static final String BOOK_FXML = "/com/ooops/lms/library_management_system/Book-view.fxml";

    private Book book;

    @FXML
    private Label authorNameLabel;

    @FXML
    private ImageView bookImage;

    @FXML
    private Label bookNameLabel;

    @FXML
    private HBox hBox;

    @FXML
    private ImageView starImage;

    protected static final ExecutorService executor = Executors.newFixedThreadPool(4);


    /**
     * thiết lập dữ liệu cho card.
     * @param otherBook sách
     */
    public void setData(Book otherBook) {
        this.book = BookManager.getInstance().isContainInAllBooks(otherBook);

        if(this.book==null) {
            this.book = otherBook;
            book.setQuantity(10);
            book.setRate(5);
        }

        // Hiển thị thông tin cơ bản
        bookNameLabel.setText(book.getTitle());
        String author = book.getAuthors().stream()
                .map(Author::getAuthorName)
                .collect(Collectors.joining(", "));
        authorNameLabel.setText(author);
        starImage.setImage(starImage(book.getRate()));

        Task<Image> loadImageTask = BookManager.getInstance().createLoadImageTask(book);

        loadImageTask.setOnSucceeded(event -> bookImage.setImage(loadImageTask.getValue()));

        executor.submit(loadImageTask);
    }

    /**
     * xem sách.
     * @param mouseEvent khi  ấn vào
     */
    public void onBookMouseClicked(MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL resource = FXMLLoaderUtil.class.getResource(BOOK_FXML);
            fxmlLoader.setLocation(resource);

            VBox newContent = fxmlLoader.load();

            BookController bookController = fxmlLoader.getController();
            if (book != null) {
                bookController.setBook(book);
            } else {
                System.err.println("Book object is null!");
            }

            fxmlLoaderUtil.updateContentBox(newContent);

            try {
                BookDAO.getInstance().add(book);
            } catch (SQLException e) {
                System.out.println("book already exist in database");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * từ sao sang image.
     * @param numOfStar số sao
     * @return Image
     */
    private Image starImage(int numOfStar) {
        String imagePath = "/image/book/" + numOfStar + "Star.png";
        if (getClass().getResourceAsStream(imagePath) == null) {
            System.out.println("Image not found: " + imagePath);
            return new Image(getClass().getResourceAsStream("/image/book/1Star.png"));
        }
        return new Image(getClass().getResourceAsStream(imagePath));
    }
}