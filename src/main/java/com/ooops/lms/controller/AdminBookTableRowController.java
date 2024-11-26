package com.ooops.lms.controller;

import com.ooops.lms.controller.BaseRowController;
import com.ooops.lms.model.Author;
import com.ooops.lms.model.Book;
import com.ooops.lms.model.Category;
import com.ooops.lms.model.enums.BookStatus;
import com.sun.mail.imap.protocol.Item;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.List;

public class AdminBookTableRowController extends BaseRowController<Book, AdminBookPageController> {


    @FXML
    private Label ISBNLabel;

    @FXML
    private Label authorNameLabel;

    @FXML
    private Label bookNameLabel;

    @FXML
    private Label categoryLabel;

    @FXML
    private Label locationLabel;

    @FXML
    private Label numberOfBookLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private HBox mainRowHbox;

    @Override
    protected void updateRowDisplay() {
        ISBNLabel.setText(String.valueOf(item.getISBN()));
        authorNameLabel.setText(getAuthors(item.getAuthors()));
        bookNameLabel.setText(item.getTitle());
        categoryLabel.setText(getCategories(item.getCategories()));
        locationLabel.setText(item.getPlaceAt());
        numberOfBookLabel.setText((String.valueOf(item.getQuantity())));
        if(item.getstatus() == null) {
            System.out.println(item.getISBN() +" is null status");
        }
        statusLabel.setText(item.getstatus()+"");
        if(item.getstatus().equals(BookStatus.AVAILABLE)) {
            statusLabel.setStyle(("-fx-text-fill: green;"));
        }
        else {
            statusLabel.setStyle(("-fx-text-fill: red;"));
        }
    }


}
