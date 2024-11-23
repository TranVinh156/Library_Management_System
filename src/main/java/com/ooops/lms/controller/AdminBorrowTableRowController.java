package com.ooops.lms.controller;

import com.ooops.lms.controller.BaseRowController;
import com.ooops.lms.model.BookIssue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdminBorrowTableRowController extends BaseRowController<BookIssue, AdminBorrowPageController> {

    @FXML
    private Label barCodeLabel;

    @FXML
    private Label bookNameLabel;

    @FXML
    private Label borrowDateLabel;

    @FXML
    private Button editButton;

    @FXML
    private Label memberIDLabel;

    @FXML
    private Label memberNameLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private HBox mainRowHbox;

    @Override
    protected void updateRowDisplay() {
        memberIDLabel.setText(String.valueOf(item.getMember().getPerson().getId()));
        memberNameLabel.setText(item.getMember().getPerson().getFirstName() + " " + item.getMember().getPerson().getLastName());
        bookNameLabel.setText(item.getBookItem().getTitle());
        barCodeLabel.setText(item.getBookItem().getBarcode() + "");
        borrowDateLabel.setText(item.getCreatedDate().substring(0, 10));
        statusLabel.setText(item.getStatus().toString());
        if(statusLabel.getText().equals("LOST")) {
            statusLabel.setStyle("-fx-text-fill: red;");
        } else {
            statusLabel.setStyle("-fx-text-fill: black;");
        }
    }

}
