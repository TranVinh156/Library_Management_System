package com.ooops.lms.controller;

import com.ooops.lms.model.BookItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AdminBookCopyRowController {

    @FXML
    private TextField barcodeText;

    @FXML
    private TextField noteText;

    @FXML
    private TextField statusText;

    private AdminBookPageController mainController;
    private BookItem bookItem;

    public void setMainController(AdminBookPageController mainController) {
        this.mainController = mainController;
    }

    /**
     * Hàm để set các thông tin các trường và bookItem của row này.
     * @param bookItem
     */
    public void setBookItem(BookItem bookItem) {
        this.bookItem = bookItem;
        barcodeText.setText(String.valueOf(bookItem.getBarcode()));
        if(bookItem.getNote() != null) {
            noteText.setText(String.valueOf(bookItem));
        } else {
            noteText.setText("-");
        }
        statusText.setText(String.valueOf(bookItem.getStatus().getDisplayName()));
    }
}
