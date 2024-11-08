package com.ooops.lms.controller;

import com.ooops.lms.util.FXMLLoaderUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import com.ooops.lms.model.Book;

public class BookCard1Controller {
    private  FXMLLoaderUtil fxmlLoaderUtil = FXMLLoaderUtil.getInstance();

    private static final String BOOK_FXML = "/com/ooops/lms/library_management_system/Book-view.fxml";

    @FXML
    private Label authorNameLabel;

    @FXML
    private ImageView bookImage;

    @FXML
    private Label bookNameLabel;

    @FXML
    private HBox hBox;

    private String [] colors = {"FFFFFF"};
//    private String [] colors = {"DFD2B4","E0FFCC","FFB1B1","DFD2B4","BEEAD8","DABEEA"};

    public void setData() {
        //Image image = new Image(getClass().getResourceAsStream(""));
        //bookImage.setImage(image);
        bookNameLabel.setText("book name");
        authorNameLabel.setText("author");
        hBox.setStyle("-fx-background-color: #" + colors[(int)(Math.random() * colors.length)]);
    }

    public void setData(Book book) {
        Image image = new Image(getClass().getResourceAsStream(book.getImagePath()));
        bookImage.setImage(image);
        bookNameLabel.setText(book.getTitle());
        authorNameLabel.setText(book.getAuthors().get(0).getAuthorName());
        hBox.setStyle("-fx-background-color: #" + colors[(int)(Math.random() * colors.length)]);
    }

    public void onBookMouseClicked(MouseEvent mouseEvent) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(BOOK_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
        }
    }
}
