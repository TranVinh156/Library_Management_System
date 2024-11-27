package com.ooops.lms.controller;

import com.ooops.lms.util.BookManager;
import com.ooops.lms.util.FXMLLoaderUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.image.Image;

import java.io.File;

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
    
    private static final String INFORMATION_FXML = "/com/ooops/lms/library_management_system/Information-view.fxml";
    private static final String HISTORY_FXML = "/com/ooops/lms/library_management_system/History-view.fxml";
    private static final String INTERFACE_SETTING_FXML = "/com/ooops/lms/library_management_system/InterfaceSetting-view.fxml";
    private static final String USER_REPORT_FXML = "/com/ooops/lms/library_management_system/UserReport-view.fxml";
    private static final String GAME_FXML = "/com/ooops/lms/library_management_system/TicTacToe-view.fxml";

    public void initialize() {
        showInfo();
    }

    /**
     * show các thông tin .
     */
    public void showInfo() {
        nameLabel.setText(UserMenuController.getMember().getPerson().getLastName() + " " +
                UserMenuController.getMember().getPerson().getFirstName());

        try {
            String imagePath = UserMenuController.getMember().getPerson().getImagePath();
            if (imagePath != null) {
                File file = new File(imagePath);
                Image image = new Image(file.toURI().toString());
                avaImage.setFill(new ImagePattern(image));
            } else {
                avaImage.setFill(Color.GRAY);
            }
        } catch (Exception e) {
            avaImage.setFill(Color.GRAY);
            e.printStackTrace();
        }

        statusLabel.setText(UserMenuController.getMember().getStatus().toString());
        borrowingLabel.setText(String.valueOf(BookManager.getInstance().getBorrowingBookSize()));
        reservedLabel.setText(String.valueOf(BookManager.getInstance().getReservedBookSize()));
    }

    public void onInfomationButtonAction(ActionEvent actionEvent) {
        VBox content = (VBox) FXMLLoaderUtil.getInstance().getInstance().loadFXML(INFORMATION_FXML);
        if (content != null) {
            FXMLLoaderUtil.getInstance().updateContentBox(content);
        } else {
            System.err.println("Failed to load Information-view.fxml");
        }
    }

    public void onHistoryButtonAction(ActionEvent actionEvent) {
        VBox content = (VBox) FXMLLoaderUtil.getInstance().loadFXML(HISTORY_FXML);
        if (content != null) {
            FXMLLoaderUtil.getInstance().updateContentBox(content);
        } else {
            System.err.println("Failed to load History-view.fxml");
        }
    }
    public void onGameButtonAction(ActionEvent actionEvent) {
        VBox content = (VBox) FXMLLoaderUtil.getInstance().loadFXML(GAME_FXML);
        if (content != null) {
            FXMLLoaderUtil.getInstance().updateContentBox(content);
        } else {
            System.err.println("Failed to load History-view.fxml");
        }
    }

    public void onInterfaceSettingButtonAction(ActionEvent actionEvent) {
        VBox content = (VBox) FXMLLoaderUtil.getInstance().loadFXML(INTERFACE_SETTING_FXML);
        if (content != null) {
            FXMLLoaderUtil.getInstance().updateContentBox(content);
        } else {
            System.err.println("Failed to load InterfaceSetting-view.fxml");
        }
    }

    public void onReportButtonAction(ActionEvent actionEvent) {
        VBox content = (VBox) FXMLLoaderUtil.getInstance().loadFXML(USER_REPORT_FXML);
        if (content != null) {
            FXMLLoaderUtil.getInstance().updateContentBox(content);
        } else {
            System.err.println("Failed to load Report-view.fxml");
        }
    }

    public void updateReservedBookSize() {
        reservedLabel.setText(String.valueOf(BookManager.getInstance().getReservedBookSize()));
    }
}
