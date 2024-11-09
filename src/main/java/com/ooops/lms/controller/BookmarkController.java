package com.ooops.lms.controller;

import com.ooops.lms.database.dao.BookMarkDAO;
import com.ooops.lms.model.Book;
import com.ooops.lms.model.BookMark;
import com.ooops.lms.util.BookManager;
import com.ooops.lms.util.FXMLLoaderUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class BookmarkController implements Initializable {
    @FXML
    private HBox suggestHBox;
    @FXML
    private HBox bookmarkHBox;

    private BookManager bookManager = BookManager.getInstance();

    private BookMarkDAO bookMarkDAO = new BookMarkDAO();

    private List<BookMark> markedBook;
    private List<Book> popularBooks;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            markedBook = bookMarkDAO.findAllBookMark(UserMenuController.member);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < markedBook.size(); i++) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/ooops/lms/library_management_system/BookCard1-view.fxml"));
                HBox cardBox = fxmlLoader.load();
                BookCard1Controller cardController = fxmlLoader.getController();
                cardController.setData(markedBook.get(i));
                bookmarkHBox.getChildren().add(cardBox);
            } catch (IOException e) {
                e.printStackTrace();  // In ra lỗi để dễ dàng theo dõi nếu gặp vấn đề
            }
        }

        popularBooks = bookManager.getPopularBooks();
        for (int i = 0; i < popularBooks.size(); i++) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/ooops/lms/library_management_system/BookCard2-view.fxml"));
                VBox cardBox = fxmlLoader.load();
                BookCard2Controller cardController = fxmlLoader.getController();
                cardController.setData(popularBooks.get(i));
                suggestHBox.getChildren().add(cardBox);
            } catch (IOException e) {
                e.printStackTrace();  // In ra lỗi để dễ dàng theo dõi nếu gặp vấn đề
            }
            if (i == 9) {
                break;
            }
        }
    }
}
