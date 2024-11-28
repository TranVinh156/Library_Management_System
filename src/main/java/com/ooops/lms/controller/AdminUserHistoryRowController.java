package com.ooops.lms.controller;

import com.ooops.lms.model.BookIssue;
import com.ooops.lms.model.BookItem;
import com.ooops.lms.model.enums.BookIssueStatus;
import com.ooops.lms.model.enums.BookStatus;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AdminUserHistoryRowController {

    @FXML
    private Label barcodeLabel;

    @FXML
    private Label bookNameLabel;

    @FXML
    private Label borrowDateLabel;

    @FXML
    private Label returnDateLabel;

    @FXML
    private Label statusLabel;

    public void setBookItem(BookIssue bookIssue) {
        barcodeLabel.setText(bookIssue.getBookItem().getBarcode()+"");
        bookNameLabel.setText(bookIssue.getBookItem().getTitle());
        //Xử lý ngày
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // hoặc định dạng phù hợp với dữ liệu của bạn
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            // Chuyển đổi và định dạng cho borrowDate
            if (bookIssue.getDueDate()!= null) {
                LocalDate createdDate = LocalDate.parse(bookIssue.getDueDate(), inputFormatter);
                borrowDateLabel.setText(createdDate.format(outputFormatter));
            } else {
                borrowDateLabel.setText(""); // Hoặc giá trị mặc định khác
            }

        } catch (DateTimeParseException e) {
            e.printStackTrace();
            // Xử lý lỗi nếu cần (ví dụ: hiển thị thông báo lỗi cho người dùng)
            borrowDateLabel.setText(""); // Hoặc giá trị mặc định khác
        }

        try {
            // Chuyển đổi và định dạng cho borrowDate
            if (bookIssue.getDueDate()!= null) {
                LocalDate createdDate = LocalDate.parse(bookIssue.getDueDate(), inputFormatter);
                returnDateLabel.setText(createdDate.format(outputFormatter));
            } else {
                returnDateLabel.setText(""); // Hoặc giá trị mặc định khác
            }

        } catch (DateTimeParseException e) {
            e.printStackTrace();
            // Xử lý lỗi nếu cần (ví dụ: hiển thị thông báo lỗi cho người dùng)
            returnDateLabel.setText(""); // Hoặc giá trị mặc định khác
        }

        statusLabel.setText(bookIssue.getStatus()+"");
        if(bookIssue.getStatus().equals(BookIssueStatus.LOST)) {
            statusLabel.setStyle(("-fx-text-fill: red;"));
        }
        else if (bookIssue.getStatus().equals(BookIssueStatus.BORROWED)) {
            statusLabel.setStyle(("-fx-text-fill: blue;"));
        } else {
            statusLabel.setStyle(("-fx-text-fill: green;"));
        }


    }

}
