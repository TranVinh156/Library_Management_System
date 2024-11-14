package com.ooops.lms.controller;

import com.ooops.lms.model.datatype.Person;
import com.ooops.lms.util.FXMLLoaderUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.image.Image;

public class SettingController {
    @FXML
    private VBox settingBox;

    @FXML
    private Circle avaImage;

    @FXML
    private Label nameLabel;

    @FXML
    private Label borrowingLabel;

    @FXML
    private Label statusLabel; // Ensure this matches the FXML file

    @FXML
    private Label reservedLabel;

    private FXMLLoaderUtil fxmlLoaderUtil = FXMLLoaderUtil.getInstance();

    private static final String INFORMATION_FXML = "/com/ooops/lms/library_management_system/Information-view.fxml";
    private static final String HISTORY_FXML = "/com/ooops/lms/library_management_system/History-view.fxml";
    private static final String INTERFACE_SETTING_FXML = "/com/ooops/lms/library_management_system/InterfaceSetting-view.fxml";
    private static final String USER_REPORT_FXML = "/com/ooops/lms/library_management_system/UserReport-view.fxml";

    public void initialize() {
        showInfo();
    }

    private void showInfo() {
        // Set user name
        nameLabel.setText(UserMenuController.member.getPerson().getLastName() + " " +
                UserMenuController.member.getPerson().getFirstName());

        // Load and display avatar image
        try {
            String imagePath = UserMenuController.member.getPerson().getImagePath();
            if (imagePath != null) {
                imagePath = imagePath.replace("//", "/");
                Image image = new Image(getClass().getResourceAsStream("/" + imagePath));
                avaImage.setFill(new ImagePattern(image));
            } else {
                avaImage.setFill(Color.GRAY);
            }
        } catch (Exception e) {
            avaImage.setFill(Color.GRAY);
            e.printStackTrace();
        }

        // Set user status and other labels
        statusLabel.setText(UserMenuController.member.getStatus().toString());
//        borrowingLabel.setText(String.valueOf(UserMenuController.member.getBorrowingCount()));
//        reservedLabel.setText(String.valueOf(UserMenuController.member.getReservedCount()));
    }

    public void onInfomationButtonAction(ActionEvent actionEvent) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(INFORMATION_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
        } else {
            System.err.println("Failed to load Information-view.fxml");
        }
    }

    public void onHistoryButtonAction(ActionEvent actionEvent) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(HISTORY_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
        } else {
            System.err.println("Failed to load History-view.fxml");
        }
    }

    public void onInterfaceSettingButtonAction(ActionEvent actionEvent) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(INTERFACE_SETTING_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
        } else {
            System.err.println("Failed to load InterfaceSetting-view.fxml");
        }
    }

    public void onReportButtonAction(ActionEvent actionEvent) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(USER_REPORT_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
        } else {
            System.err.println("Failed to load InterfaceSetting-view.fxml");
        }
    }
}
