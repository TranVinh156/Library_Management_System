package com.ooops.lms.controller;

import com.ooops.lms.model.Author;
import com.ooops.lms.model.BookMark;
import com.ooops.lms.model.BookReservation;
import com.ooops.lms.util.BookManager;
import com.ooops.lms.util.FXMLLoaderUtil;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import com.ooops.lms.model.Book;
import javafx.scene.paint.ImagePattern;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.ooops.lms.controller.BookSuggestionCardController.executor;

public class BookCard1Controller {
    private  FXMLLoaderUtil fxmlLoaderUtil = FXMLLoaderUtil.getInstance();

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

    private String [] colors = {"DABEEA","E0FFCC"};

    /**
     * thiết lập dữ liệu cho book card.
     * @param book sách truyền vào
     */
    public void setData(Book book) {
        this.book = book;
        bookNameLabel.setText(book.getTitle());
        String author = "";
        List<Author> authorList = book.getAuthors();
        for(int i = 0;i<authorList.size();i++) {
            author += authorList.get(i).getAuthorName() + ",";
        }
        authorNameLabel.setText(author);
        starImage.setImage(starImage(book.getRate()));
        hBox.setStyle("-fx-background-color: #" + colors[(int)(Math.random() * colors.length)]);

        Task<Image> loadImageTask = BookManager.getInstance().createLoadImageTask(book);


        loadImageTask.setOnSucceeded(event -> bookImage.setImage(loadImageTask.getValue()));

        executor.submit(loadImageTask);
    }

    /**
     * ấn vào sách.
     * @param mouseEvent khi chuột trỏ vào
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * chuyển từ rate sang Inage.
     * @param numOfStar số rate
     * @return ảnh của rate
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
