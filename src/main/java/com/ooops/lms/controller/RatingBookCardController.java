package com.ooops.lms.controller;

import com.ooops.lms.model.Book;
import com.ooops.lms.model.BookItem;
import com.ooops.lms.model.Report;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;


public class RatingBookCardController {
    private Book book;

    @FXML
    private Label bookNameText,authorNameText,statusText;


    public void setData(Book bookItem) {
//        this.book =bookItem;
//        bookNameText.setText(bookItem.getb);
//        statusText.setText(bookItem.getStatus().toString());
    }

    public void onShowReportMouseClicked(MouseEvent mouseEvent) {
    }
}
