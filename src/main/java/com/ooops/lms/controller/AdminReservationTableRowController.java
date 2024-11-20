package com.ooops.lms.controller;

import com.ooops.lms.model.BookIssue;
import com.ooops.lms.model.BookReservation;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class AdminReservationTableRowController extends BaseRowController<BookReservation, AdminReservationPageController> {

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
    }

}
