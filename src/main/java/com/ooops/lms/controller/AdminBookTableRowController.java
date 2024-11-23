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
        statusLabel.setText(item.getstatus().getDisplayName());
        if(item.getstatus().equals(BookStatus.AVAILABLE)) {
            statusLabel.setStyle(("-fx-text-fill: green;"));
        }
        else {
            statusLabel.setStyle(("-fx-text-fill: red;"));
        }
    }

    public String getCategories(List<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            return "Không có danh mục"; // Trả về thông báo nếu không có danh mục
        }

        StringBuilder result = new StringBuilder(); // Sử dụng StringBuilder để xây dựng chuỗi

        for (int i = 0; i < categories.size(); i++) {
            // Giả sử mỗi Category có phương thức getName()
            result.append(categories.get(i).getCatagoryName());

            // Nếu không phải là phần tử cuối cùng, thêm dấu phẩy
            if (i < categories.size() - 1) {
                result.append(", ");
            }
        }

        return result.toString(); // Chuyển đổi StringBuilder thành String
    }

    public String getAuthors(List<Author> authors) {
        if (authors == null || authors.isEmpty()) {
            return "Không có danh mục"; // Trả về thông báo nếu không có danh mục
        }

        StringBuilder result = new StringBuilder(); // Sử dụng StringBuilder để xây dựng chuỗi

        for (int i = 0; i < authors.size(); i++) {
            result.append(authors.get(i).getAuthorName());

            if (i < authors.size() - 1) {
                result.append(", ");
            }
        }

        return result.toString(); // Chuyển đổi StringBuilder thành String
    }
}
