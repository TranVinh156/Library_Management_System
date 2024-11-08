package com.ooops.lms.controller;

import com.ooops.lms.util.FXMLLoaderUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdvancedSearchController implements Initializable {
    @FXML
    private HBox hBox1;

    @FXML
    private HBox hBox2;

    @FXML
    private HBox hBox3;

    private FXMLLoaderUtil fxmlLoaderUtil = FXMLLoaderUtil.getInstance();

    private static final String DASHBOARD_FXML = "/com/ooops/lms/library_management_system/DashBoard-view.fxml";

    @FXML
    private ChoiceBox<String> categoryChoiceBox;

    public void onBackButtonAction(ActionEvent event) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(DASHBOARD_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
        }
    }

    public void onSearchButtonAction(ActionEvent event) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        categoryChoiceBox.getItems().addAll("tất cả","hot nhất", "mới nhất", "dở nhất", "tác giả","không phải sách","210");

        categoryChoiceBox.setValue("tất cả");
        try {
            for(int i = 0;i<2;i++) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/com/ooops/lms/library_management_system/BookCard1-view.fxml"));
                    HBox cardBox = fxmlLoader.load();
                    BookCard1Controller cardController = fxmlLoader.getController();
                    cardController.setData();
                    hBox1.getChildren().add(cardBox);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            for(int i = 0;i<2;i++) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/com/ooops/lms/library_management_system/BookCard1-view.fxml"));
                    HBox cardBox = fxmlLoader.load();
                    BookCard1Controller cardController = fxmlLoader.getController();
                    cardController.setData();
                    hBox2.getChildren().add(cardBox);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            for(int i = 0;i<2;i++) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/com/ooops/lms/library_management_system/BookCard1-view.fxml"));
                    HBox cardBox = fxmlLoader.load();
                    BookCard1Controller cardController = fxmlLoader.getController();
                    cardController.setData();
                    hBox3.getChildren().add(cardBox);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
