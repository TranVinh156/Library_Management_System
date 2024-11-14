package com.ooops.lms.controller;

import com.ooops.lms.database.dao.BookDAO;
import com.ooops.lms.model.Book;
import com.ooops.lms.util.BookManager;
import com.ooops.lms.util.FXMLLoaderUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class DashBoardController implements Initializable {
    @FXML
    private VBox dashboardBox;
    @FXML
    private HBox popularHBox;
    @FXML
    private HBox highRankHBox;

    private FXMLLoaderUtil fxmlLoaderUtil = FXMLLoaderUtil.getInstance();

    private BookManager bookManager = BookManager.getInstance();

    private List<Book> popularBooks;
    private List<Book> highRankBooks;

    private static final String MORE_BOOK_FXML = "/com/ooops/lms/library_management_system/MoreBook-view.fxml";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        popularBooks = bookManager.getPopularBooks();

        for (int i = 0; i < popularBooks.size(); i++) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/ooops/lms/library_management_system/BookCard1-view.fxml"));
                HBox cardBox = fxmlLoader.load();
                BookCard1Controller cardController = fxmlLoader.getController();
                cardController.setData(popularBooks.get(i));
                popularHBox.getChildren().add(cardBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (i == 9) {
                break;
            }
        }

        highRankBooks = bookManager.getHighRankBooks();

        for (int i = 0; i < highRankBooks.size(); i++) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/ooops/lms/library_management_system/BookCard2-view.fxml"));
                VBox cardBox = fxmlLoader.load();
                BookCard2Controller cardController = fxmlLoader.getController();
                cardController.setData(highRankBooks.get(i));
                highRankHBox.getChildren().add(cardBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (i == 9) {
                break;
            }
        }
    }

    public void onMoreButtonAction(ActionEvent event) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(MORE_BOOK_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
        }
    }

}
