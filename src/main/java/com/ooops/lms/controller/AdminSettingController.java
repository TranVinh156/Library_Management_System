package com.ooops.lms.controller;

import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.Settings.SettingManger;
import com.ooops.lms.Settings.Settings;
import com.ooops.lms.faceid.FaceidRecognizer;
import com.ooops.lms.faceid.FaceidUnregister;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AdminSettingController {

    @FXML
    private Button changePasswordButton;

    @FXML
    private Button closeButton;

    @FXML
    private Button contactButton;

    @FXML
    private Button generalSettingButton;

    @FXML
    private AnchorPane generalSettingPane;

    @FXML
    private Button hihiButton;

    @FXML
    private AnchorPane hihiPane;

    @FXML
    private ImageView imageIBView;

    @FXML
    private Label ibLabel;

    @FXML
    private Button securityButton;

    @FXML
    private AnchorPane securityPane;

    @FXML
    private Button setFaceIDButton;

    @FXML
    private ChoiceBox<String> themeBox;

    private Button currentActiveButton = null;

    private SettingManger settingManger;
    private Settings settings;
    private int adminID;

    public void setAdminID(int adminID) {
        this.adminID = adminID;
        themeBox.getItems().addAll("Normal", "Dark", "Pink", "Gold");

        themeBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (!themeBox.getSelectionModel().getSelectedItem().equals("Normal")) {
                CustomerAlter.showMessage("Muốn sử dụng chức năng này thì hãy nạp Vip nhé");
                themeBox.setValue("Normal");
            }
        });

        settingManger = new SettingManger();
        settings = settingManger.loadSettings();

        if (settings == null) {
            settings = new Settings("Normal", 50, "chua co", false);
        }

        themeBox.setValue(settings.getMode().toString());
        settings.setHaveFaceID(isHaveFaceID(adminID));
        if (!settings.isHaveFaceID()) {
            setFaceIDButton.setText("Thiết lập faceID");
            setFaceIDButton.setStyle("-fx-text-fill: black;");
        } else {
            setFaceIDButton.setText("Xóa faceID");
            setFaceIDButton.setStyle("-fx-text-fill: red;");
        }
        
    }

    @FXML
    void onContactButtonAction(ActionEvent event) {
        imageIBView.setVisible(true);
        ibLabel.setVisible(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(3.5));
        pause.setOnFinished(event1 -> {
            imageIBView.setVisible(false);
            ibLabel.setVisible(false);
        });
        pause.play();
    }

    @FXML
    void onThemeBoxButtonAction(MouseEvent event) {

    }

    public void initialize() {

    }

    @FXML
    void onChangePasswordButtonAction(ActionEvent event) {
        CustomerAlter.showMessage("Oh no lỗi rồi. Hãy vào mục support để bọn tớ trợ giúp cậu nhé!");
    }

    @FXML
    void onGeneralSettingButtonAction(ActionEvent event) {
        hihiPane.setVisible(false);
        generalSettingPane.setVisible(true);
        securityPane.setVisible(false);
        setActiveButton(generalSettingButton);
    }

    @FXML
    void onHihiButtonAction(ActionEvent event) {
        hihiPane.setVisible(true);
        generalSettingPane.setVisible(false);
        securityPane.setVisible(false);
        setActiveButton(hihiButton);
    }

    @FXML
    void onSecurityButtonAction(ActionEvent event) {
        hihiPane.setVisible(false);
        generalSettingPane.setVisible(false);
        securityPane.setVisible(true);
        setActiveButton(securityButton);
    }

    @FXML
    void onSetFaceIDButtonAction(ActionEvent event) {
        if (setFaceIDButton.getText().equals("Thiết lập faceID")) {
            setFaceID(adminID);
        } else if (setFaceIDButton.getText().equals("Xóa faceID")) {
            FaceidUnregister.unregisterUser(adminID+"", FaceidUnregister.ADMIN);
            setFaceIDButton.setText("Thiết lập faceID");
            setFaceIDButton.setStyle("-fx-text-fill: black;");
            settings.setHaveFaceID(false);
            settingManger.saveSettings(settings);
        }
    }

    @FXML
    void onCloseButtonAction(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void reset() {
        hihiPane.setVisible(false);
        generalSettingPane.setVisible(true);
        securityPane.setVisible(false);
        setActiveButton(generalSettingButton);
    }

    private void setActiveButton(Button button) {
        if (currentActiveButton != null) {
            // Reset lại kiểu dáng của nút trước đó
            currentActiveButton.setStyle("");
        }

        // Gán nút hiện tại vào trạng thái đang hoạt động
        currentActiveButton = button;

        // Đặt kiểu dáng in đậm màu cho nút đang được chọn
        currentActiveButton.setStyle("-fx-background-color: #FFF;"); // Kiểu CSS
    }

    private boolean isHaveFaceID(int adminID) {
        try {
            // Đường dẫn tới thư mục chứa dữ liệu FaceID chung
            Path faceIDFolder = (Path) Paths.get("face/training_data");
            // Đường dẫn tới thư mục riêng của adminID
            Path newFaceIDFolder = faceIDFolder.resolve(String.valueOf(adminID));

            // Kiểm tra nếu thư mục chung không tồn tại thì tạo nó
            if (Files.notExists( faceIDFolder)) {
                Files.createDirectories(faceIDFolder);
            }

            // Kiểm tra thư mục FaceID của adminID
            if (Files.exists(newFaceIDFolder)) {
                return true; // Thư mục đã tồn tại
            } else {
                System.out.println("Khong coooooooo");
                return false;
            }
        } catch (Exception e) {
            // Xử lý ngoại lệ (nếu có lỗi)
            e.printStackTrace();
            return false;
        }
    }

    private void setFaceID(int adminID) {
        try {
            // Đường dẫn tới thư mục chứa dữ liệu FaceID chung
            Path faceIDFolder = (Path) Paths.get("face/training_data");
            // Đường dẫn tới thư mục riêng của adminID
            Path newFaceIDFolder = faceIDFolder.resolve(String.valueOf(adminID));

            // Kiểm tra nếu thư mục chung không tồn tại thì tạo nó
            if (Files.notExists( faceIDFolder)) {
                Files.createDirectories(faceIDFolder);
            }

            // Kiểm tra thư mục FaceID của adminID
            if (!Files.exists(newFaceIDFolder)) {
                Files.createDirectories(newFaceIDFolder);
            }
            FaceidRecognizer.registerUser(adminID+"", FaceidRecognizer.ADMIN);
            setFaceIDButton.setText("Xóa faceID");
            setFaceIDButton.setStyle("-fx-text-fill: red;");
            settings.setHaveFaceID(true);
            settingManger.saveSettings(settings);


        } catch (Exception e) {
            // Xử lý ngoại lệ (nếu có lỗi)
            e.printStackTrace();
        }
    }

}
