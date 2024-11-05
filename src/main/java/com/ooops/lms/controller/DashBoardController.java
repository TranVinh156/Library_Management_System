package com.ooops.lms.controller;

import com.ooops.lms.util.FXMLLoaderUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashBoardController implements Initializable {
    @FXML
    private VBox dashboardBox;
    @FXML
    private HBox popularHBox;
    @FXML
    private HBox latestHBox;

    private FXMLLoaderUtil fxmlLoaderUtil = FXMLLoaderUtil.getInstance();

    private static final String MORE_BOOK_FXML = "/com/ooops/lms/library_management_system/MoreBook-view.fxml";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (int i = 0; i < 5; i++) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/ooops/lms/library_management_system/BookCard1-view.fxml"));
                HBox cardBox = fxmlLoader.load();
                BookCard1Controller cardController = fxmlLoader.getController();
                cardController.setData();
                popularHBox.getChildren().add(cardBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < 10; i++) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/ooops/lms/library_management_system/BookCard2-view.fxml"));
                VBox cardBox = fxmlLoader.load();
                BookCard2Controller cardController = fxmlLoader.getController();
                cardController.setData();
                latestHBox.getChildren().add(cardBox);
            } catch (IOException e) {
                e.printStackTrace();
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
