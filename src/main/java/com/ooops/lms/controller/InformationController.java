package com.ooops.lms.controller;

import com.ooops.lms.util.FXMLLoaderUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.control.ChoiceBox;

public class InformationController {
    @FXML
    private VBox contentBox;

    @FXML
    private ChoiceBox<String> genderChoiceBox;

    private static final String SETTING_FXML = "/com/ooops/lms/library_management_system/Setting-view.fxml";

    private FXMLLoaderUtil fxmlLoaderUtil = FXMLLoaderUtil.getInstance();

    public void onBackButtonAction(ActionEvent actionEvent) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(SETTING_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
        }
    }

    public void initialize() {
        genderChoiceBox.getItems().addAll("nam", "nữ", "đoán xem");

        genderChoiceBox.setValue("không biết");
    }

}
