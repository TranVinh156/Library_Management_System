package com.ooops.lms.controller;

import com.ooops.lms.database.dao.AccountDAO;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLOutput;

public class LoginController {

    @FXML
    private Button forgotPasswordButton;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordText;

    @FXML
    private Button registerButton;

    @FXML
    private Button swichButton;

    @FXML
    public ImageView imageStatus;

    @FXML
    private Rectangle switchBar;

    @FXML
    private TextField usernameText;
    @FXML
    private StackPane mainPane;

    private boolean isUser = true;
    private AccountDAO accountDAO = new AccountDAO();

    public void initialize() {
        setSwitchBar();
        setImageStatus();
    }

    @FXML
    void onForgotPasswordButtonAction(ActionEvent event) {
        openForgotPasswordView();
    }

    @FXML
    void onLoginButtonAction(ActionEvent event) {
        String username = usernameText.getText();
        String password = passwordText.getText();

        try {
            if (isUser) {
                handleUserLogin(username, password);
            } else {
                handleAdminLogin(username, password);
            }
        } catch (SQLException e) {
            System.out.println("Đã xảy ra lỗi: " + e.getMessage());
        }
    }

    private void handleUserLogin(String username, String password) throws SQLException {
        if (accountDAO.validateMemberLogin(username, password)) {
            System.out.println("Login Successful");
            openUser();
        } else {
            System.out.println("Thông tin đăng nhập không chính xác.");
        }
    }

    private void handleAdminLogin(String username, String password) throws SQLException {
        if (accountDAO.validateAdminLogin(username, password)) {
            System.out.println("Login Successful");
            openAdmin();
        } else {
            System.out.println("Thông tin đăng nhập không chính xác.");
        }
    }

    @FXML
    void onRegisterButtonAction(ActionEvent event) {
        openRegisterView();
    }

    @FXML
    void onSwitchButtonAction(ActionEvent event) {
        isUser = !isUser;
        setSwitchBar();
        setImageStatus();
    }

    private void openForgotPasswordView() {
        loadView("/com/ooops/lms/library_management_system/ForgotPassword-view.fxml",false);
    }

    private void openAdmin() {
        try {
            // Tải cửa sổ đăng ký
            Stage stage = (Stage) registerButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/com/ooops/lms/library_management_system/AdminMenu.fxml"));
            stage.setResizable(true);
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void openUser() {

    }

    private void openRegisterView() {
        loadView("/com/ooops/lms/library_management_system/UserResign-view.fxml",false);
    }

    private void loadView(String fxmlPath, boolean resizable) {
        try {

            // Tải cửa sổ đăng ký
            Stage stage = (Stage) registerButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            stage.setResizable(resizable);
            stage.setScene(new Scene(root));

        } catch (IOException e) {
            System.out.println("Lỗi khi tải giao diện: " + e.getMessage());
        }
    }

    private void setSwitchBar() {
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(switchBar);
        transition.setDuration(Duration.seconds(0.5));
        transition.setToX(isUser ? 0 : -55); // Vị trí cho chế độ
        transition.play();
    }

    private void setImageStatus() {
        String imagePath = isUser ? "file:src/main/resources/image/customer/login/User.gif" : "file:src/main/resources/image/customer/login/Admin.gif";
        imageStatus.setImage(new Image(imagePath));
    }

}
