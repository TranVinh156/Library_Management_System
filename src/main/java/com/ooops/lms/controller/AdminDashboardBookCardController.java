package com.ooops.lms.controller;

import com.ooops.lms.Cache.ImageCache;
import com.ooops.lms.model.Author;
import com.ooops.lms.model.Book;
import com.ooops.lms.util.BookManager;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.List;

import static com.ooops.lms.controller.BookSuggestionCardController.executor;

public class AdminDashboardBookCardController {

    @FXML
    private Label authorNameLabel;

    @FXML
    private ImageView bookImage;

    @FXML
    private Label bookNameLabel;

    @FXML
    private VBox vboxMain;

    @FXML
    private ImageView starImage;

    private AdminBookPageController mainController;
    private AdminMenuController adminMenuController;

    private Book book;

    public void setItem(Book book) {
        this.book = book;
        setupRowClickHandler();
        bookNameLabel.setText(book.getTitle());
        String author = "";
        List<Author> authorList = book.getAuthors();
        for(int i = 0;i<authorList.size();i++) {
            author += authorList.get(i).getAuthorName() + ",";
        }
        authorNameLabel.setText(author);
        if(book.getRate() == 0) {
            book.setRate(BookManager.getInstance().isContainInAllBooks(book).getRate());
        }
        starImage.setImage(starImage(book.getRate()));

        // Tải ảnh bất đồng bộ
        Task<Image> loadImageTask = new Task<>() {
            @Override
            protected Image call() throws Exception {
                try {
                    Image image = ImageCache.getImageLRUCache().get(book.getImagePath());
                    if(image != null) {
                        return image;
                    } else {
                        Image image1 = new Image(book.getImagePath(), true);
                        ImageCache.getImageLRUCache().put(book.getImagePath(), image1);
                        return new Image(image1.getUrl());
                    }
                } catch (Exception e) {
                    System.out.println("Length: " + book.getImagePath().length());

                    File file = new File("bookImage/default.png");
                    return new Image(file.toURI().toString());
                }
            }
        };

        loadImageTask.setOnSucceeded(event -> bookImage.setImage(loadImageTask.getValue()));

        executor.submit(loadImageTask);


    }
    private Image starImage(int numOfStar) {
        String imagePath = "/image/book/" + numOfStar + "Star.png";
        if (getClass().getResourceAsStream(imagePath) == null) {
            System.out.println("Image not found: " + imagePath);
            return new Image(getClass().getResourceAsStream("/image/book/1Star.png"));
        }
        return new Image(getClass().getResourceAsStream(imagePath));
    }

    private void setupRowClickHandler() {
        vboxMain.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                handleRowClick();
            }
        });
    }

    protected void handleRowClick() {
        this.adminMenuController.onBookManagmentButtonAction(new ActionEvent());
        mainController.loadDetail(this.book);
    }

    public void setMainController(AdminBookPageController mainController) {
        this.mainController = mainController;
    }

    public void setAdminMenuController(AdminMenuController adminMenuController) {
        this.adminMenuController = adminMenuController;
    }

}
