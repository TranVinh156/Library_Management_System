package com.ooops.lms.controller;

import com.ooops.lms.model.Author;
import com.ooops.lms.model.Book;
import com.ooops.lms.model.Category;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.List;

public class AdminBookTableRowController extends BasicBookController {

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

    private AdminBookPageController mainController;

    private Book book;

    public void setMainController(AdminBookPageController controller) {
        this.mainController = controller;
    }

    public void initialize() {
        mainRowHbox.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                handleClickRow();
            }
        });
    }

    public void handleClickRow() {
        System.out.println("Click CLick");
        mainController.loadDetail(book);
    }

    public void resetRowColor() {
        mainRowHbox.setStyle(""); // Khôi phục lại màu nền mặc định
    }

    public void setBook(Book book) {
        this.book = book;
        ISBNLabel.setText(String.valueOf(book.getISBN()));
        authorNameLabel.setText(getAuthors(book.getAuthors()));
        bookNameLabel.setText(book.getTitle());
        categoryLabel.setText(getCategories(book.getCategories()));
        locationLabel.setText(book.getPlaceAt());
        numberOfBookLabel.setText((String.valueOf(book.getQuantity())));
        statusLabel.setText((book.getQuantity() - book.getNumberOfLoanedBooks() > 0) ? "Có sẵn" : "Hết sách");
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
