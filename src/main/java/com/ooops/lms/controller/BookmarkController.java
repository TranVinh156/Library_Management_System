package com.ooops.lms.controller;

import com.ooops.lms.util.FXMLLoaderUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BookmarkController implements Initializable {
    @FXML
    private HBox suggestHBox;
    @FXML
    private HBox bookmarkHBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (int i = 0; i < 5; i++) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/ooops/lms/library_management_system/BookCard1-view.fxml"));
                HBox cardBox = fxmlLoader.load();
                BookCard1Controller cardController = fxmlLoader.getController();
                cardController.setData();
                bookmarkHBox.getChildren().add(cardBox);
            } catch (IOException e) {
                e.printStackTrace();  // In ra lỗi để dễ dàng theo dõi nếu gặp vấn đề
            }
        }

        for (int i = 0; i < 10; i++) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/ooops/lms/library_management_system/BookCard2-view.fxml"));
                VBox cardBox = fxmlLoader.load();
                BookCard2Controller cardController = fxmlLoader.getController();
                cardController.setData();
                suggestHBox.getChildren().add(cardBox);
            } catch (IOException e) {
                e.printStackTrace();  // In ra lỗi để dễ dàng theo dõi nếu gặp vấn đề
            }
        }
    }
}
