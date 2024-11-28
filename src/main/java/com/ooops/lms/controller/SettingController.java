package com.ooops.lms.controller;

import com.ooops.lms.faceid.FaceidRecognizer;
import com.ooops.lms.faceid.FaceidUnregister;
import com.ooops.lms.userInfo.UserInfoManagement;
import com.ooops.lms.util.BookManager;
import com.ooops.lms.util.FXMLLoaderUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.scene.image.Image;
import org.opencv.videoio.VideoCapture;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SettingController {
    @FXML
    private VBox settingBox,cameraVBox;

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

    @FXML
    AnchorPane faceIDPane;

    @FXML
    Button faceIDButton;

    @FXML
    ImageView cameraImage;
    
    private static final String INFORMATION_FXML = "/com/ooops/lms/library_management_system/Information-view.fxml";
    private static final String HISTORY_FXML = "/com/ooops/lms/library_management_system/History-view.fxml";
    private static final String INTERFACE_SETTING_FXML = "/com/ooops/lms/library_management_system/InterfaceSetting-view.fxml";
    private static final String USER_REPORT_FXML = "/com/ooops/lms/library_management_system/UserReport-view.fxml";
    private static final String GAME_FXML = "/com/ooops/lms/library_management_system/TicTacToe-view.fxml";

    private static final String SET_FACEID = "Thiết lập FaceID";
    private static final String DELETE_FACEID = "Xoá FaceID";

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

        if(UserMenuController.getUserInfo().isFaceIdSet()) {
            faceIDButton.setText(DELETE_FACEID);
//            faceIDButton.setStyle(-b);
        }
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
    private VideoCapture camera;
    private boolean isCameraActive = true;
    public void onFaceIDButtonAction(ActionEvent actionEvent) {
        faceIDPane.setVisible(true);
    }

    public void onSetFaceIDButtonAction(ActionEvent actionEvent) {
        System.out.println(UserMenuController.getMember().getAccountId());
        if (faceIDButton.getText().equals(SET_FACEID)) {
            setFaceID(UserMenuController.getMember().getAccountId());
            UserMenuController.getUserInfo().setFaceIdSet(true);
            UserInfoManagement.getInstance().updateUserInfo(UserMenuController.getUserInfo());
            faceIDButton.setText(DELETE_FACEID);
        } else {
            FaceidUnregister.unregisterUser(UserMenuController.getMember().getAccountId()+"", 2);
            faceIDButton.setText(SET_FACEID);
//            faceIDButton.setStyle("-fx-text-fill: black;");
//            settings.setHaveFaceID(false);
//            settingManger.saveSettings(settings);
            UserMenuController.getUserInfo().setFaceIdSet(false);
            UserInfoManagement.getInstance().updateUserInfo(UserMenuController.getUserInfo());
        }
    }

    public void updateReservedBookSize() {
        reservedLabel.setText(String.valueOf(BookManager.getInstance().getReservedBookSize()));
    }

    public void onCloseFaceIDButtonAction(ActionEvent actionEvent) {
        faceIDPane.setVisible(false);
    }

    private void setFaceID(int accountID) {
        try {
            // Đường dẫn tới thư mục chứa dữ liệu FaceID chung
            Path faceIDFolder = (Path) Paths.get("face/training_data");
            // Đường dẫn tới thư mục riêng của adminID
            Path newFaceIDFolder = faceIDFolder.resolve(String.valueOf(accountID));

            // Kiểm tra nếu thư mục chung không tồn tại thì tạo nó
            if (Files.notExists( faceIDFolder)) {
                Files.createDirectories(faceIDFolder);
            }

            // Kiểm tra thư mục FaceID của adminID
            if (!Files.exists(newFaceIDFolder)) {
                Files.createDirectories(newFaceIDFolder);
            }
            FaceidRecognizer.registerUser(accountID+"", FaceidRecognizer.USER);

        } catch (Exception e) {
            // Xử lý ngoại lệ (nếu có lỗi)
            e.printStackTrace();
        }
    }
}
