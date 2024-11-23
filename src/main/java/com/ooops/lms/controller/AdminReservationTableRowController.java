package com.ooops.lms.controller;

import com.ooops.lms.model.BookIssue;
import com.ooops.lms.model.BookReservation;
import com.ooops.lms.model.enums.BookReservationStatus;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // hoặc định dạng phù hợp với dữ liệu của bạn
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            // Chuyển đổi và định dạng cho borrowDate
            if (item.getCreatedDate() != null) {
                LocalDate createdDate = LocalDate.parse(item.getCreatedDate(), inputFormatter);
                borrowDateLabel.setText(createdDate.format(outputFormatter));
            } else {
                borrowDateLabel.setText(""); // Hoặc giá trị mặc định khác
            }

        } catch (DateTimeParseException e) {
            e.printStackTrace();
            // Xử lý lỗi nếu cần (ví dụ: hiển thị thông báo lỗi cho người dùng)
            borrowDateLabel.setText(""); // Hoặc giá trị mặc định khác
        }
        statusLabel.setText(item.getStatus().toString());
        if(item.getStatus().equals(BookReservationStatus.CANCELED)) {
            statusLabel.setStyle("-fx-text-fill: red;");
        } else if(item.getStatus().equals(BookReservationStatus.WAITING)) {
            statusLabel.setStyle("-fx-text-fill: blue");
        } else{
            statusLabel.setStyle("-fx-text-fill: black;");
        }
    }

}
