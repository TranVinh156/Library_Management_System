package com.ooops.lms.controller;

import com.ooops.lms.model.Author;
import com.ooops.lms.model.Book;
import com.ooops.lms.util.FXMLLoaderUtil;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static com.ooops.lms.controller.BookSuggestionCardController.executor;

public class BookRankingCardController {
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
    private Label ranking;

    @FXML
    private HBox hBox;

    @FXML
    private ImageView starImage;

    public void setData(Book book,String rank) {
        this.book = book;
        bookNameLabel.setText("book name");
        authorNameLabel.setText("author");
        bookNameLabel.setText(book.getTitle());
        String author = "";
        List<Author> authorList = book.getAuthors();
        for(int i = 0;i<authorList.size();i++) {
            author += authorList.get(i).getAuthorName() + ",";
        }
        authorNameLabel.setText(author);
        starImage.setImage(starImage(book.getRate()));
        ranking.setText(rank);

        Task<Image> loadImageTask = new Task<>() {
            @Override
            protected Image call() throws Exception {
                try {
                    return new Image(book.getImagePath(), true);
                } catch (Exception e) {
                    System.out.println("Length: " + book.getImagePath().length());

                    File file = new File("Library_Management_System/bookImage/default.png");
                    return new Image(file.toURI().toString());
                }
            }
        };

        loadImageTask.setOnSucceeded(event -> bookImage.setImage(loadImageTask.getValue()));

        executor.submit(loadImageTask);
    }

    public void onReadButtonAction(ActionEvent actionEvent) {
        try {
            // Load FXML
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL resource = FXMLLoaderUtil.class.getResource(BOOK_FXML);
            fxmlLoader.setLocation(resource);

            VBox newContent = fxmlLoader.load();

            BookController bookController = fxmlLoader.getController();
            if (book != null) {
                bookController.setBook(book);
                bookController.setData();
            } else {
                System.err.println("Book object is null!");
            }

            fxmlLoaderUtil.updateContentBox(newContent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Image starImage(int numOfStar) {
        String imagePath = "/image/book/" + numOfStar + "Star.png";
        if (getClass().getResourceAsStream(imagePath) == null) {
            System.out.println("Image not found: " + imagePath);
            return new Image(getClass().getResourceAsStream("/image/book/1Star.png"));
        }

        return new Image(getClass().getResourceAsStream(imagePath));
    }
}
