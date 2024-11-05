package com.ooops.lms.controller;

import com.ooops.lms.util.FXMLLoaderUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class BookController {
    private static final String DASHBOARD_FXML = "/com/ooops/lms/library_management_system/DashBoard-view.fxml";
    private FXMLLoaderUtil fxmlLoaderUtil = FXMLLoaderUtil.getInstance();

    @FXML
    VBox bookBox;

    public void onBackButtonAction(ActionEvent event) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(DASHBOARD_FXML);
        if (content != null) {
            // Remove content if it's already in the scene graph
            if (bookBox.getChildren().contains(content)) {
                bookBox.getChildren().remove(content);
            }
            fxmlLoaderUtil.updateContentBox(content);
        }
    }

    public void onReserveBookButtonAction(ActionEvent actionEvent) {
    }

    public void onBookmarkButtonAction(ActionEvent actionEvent) {
    }
}