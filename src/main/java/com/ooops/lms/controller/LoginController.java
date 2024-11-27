package com.ooops.lms.controller;


import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.Command.Command;
import com.ooops.lms.Command.CommandInvoker;
import com.ooops.lms.Command.LoginCommand;
import com.ooops.lms.database.dao.AccountDAO;
import com.ooops.lms.faceid.FaceidRecognizer;
import com.ooops.lms.model.enums.Role;
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


public class LoginController extends BasicController {

    @FXML
    private Button forgotPasswordButton;

    @FXML
    private Button loginButton;

    @FXML
    private Button faceIDButton;


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

    private Role role = Role.NONE;

    public void initialize() {
        setSwitchBar();
        setImageStatus();
    }

    private void switchRole() {
        if (role.equals(Role.ADMIN)) {
            role = Role.NONE;
        } else if (role.equals(Role.NONE)) {
            role = Role.ADMIN;
        }
    }

    @FXML
    void onForgotPasswordButtonAction(ActionEvent event) {
        openForgotPasswordView();
    }

    @FXML
    void onLoginButtonAction(ActionEvent event) {
        String username = usernameText.getText();
        String password = passwordText.getText();
        if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
            Stage stage = (Stage) registerButton.getScene().getWindow();

            Command loginCommand = new LoginCommand(stage, role, username, password);
            commandInvoker.setCommand(loginCommand);
            commandInvoker.executeCommand();

        } else {
            CustomerAlter.showMessage("Không được để trống!");
        }
    }

    @FXML
    void onRegisterButtonAction(ActionEvent event) {
        openRegisterView();
    }

    @FXML
    void onSwitchButtonAction(ActionEvent event) {
        switchRole();
        setSwitchBar();
        setImageStatus();
    }

    @FXML
    void onFaceIDButtonAction(ActionEvent event) {
        if (role.equals(Role.ADMIN)) {
            try {
                if (AccountDAO.getInstance().adminLoginByFaceID() > 0) {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/ooops/lms/library_management_system/AdminMenu.fxml"));
                    Parent root = fxmlLoader.load();
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) registerButton.getScene().getWindow();
                    stage.setResizable(true);
                    stage.setWidth(stage.getWidth());
                    stage.setHeight(stage.getHeight());
                    stage.setScene(scene);
                    stage.show();
                } else {
                    CustomerAlter.showMessage("Không nhận ra khuôn mặt.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                int memberID = AccountDAO.getInstance().userLoginByFaceID();
                if (memberID > 0) {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/ooops/lms/library_management_system/UserMenu-view.fxml"));
                    Parent root = fxmlLoader.load();

                    UserMenuController userMenu = fxmlLoader.getController();
                    Stage stage = (Stage) registerButton.getScene().getWindow();
                    userMenu.setMemberID(memberID);
                    stage.setResizable(true);
                    stage.setWidth(stage.getWidth());
                    stage.setHeight(stage.getHeight());
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } else {
                    CustomerAlter.showMessage("Không nhận ra khuôn mặt.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void openForgotPasswordView() {
        loadView("/com/ooops/lms/library_management_system/ForgotPassword-view.fxml", false);
    }

    private void openRegisterView() {
        loadView("/com/ooops/lms/library_management_system/UserResign-view.fxml", false);
    }

    private void loadView(String fxmlPath, boolean resizable) {
        try {

            // Tải cửa sổ đăng ký
            Stage stage = (Stage) registerButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            stage.setResizable(true);
            stage.setWidth(stage.getWidth());
            stage.setHeight(stage.getHeight());
            stage.setScene(new Scene(root));

        } catch (IOException e) {
            System.out.println("Lỗi khi tải giao diện: " + e.getMessage());
        }
    }

    private void setSwitchBar() {
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(switchBar);
        transition.setDuration(Duration.seconds(0.5));
        if (role.equals(Role.ADMIN)) {
            transition.setToX(-55);
        } else {
            transition.setToX(0);
        }
        transition.play();
    }

    private void setImageStatus() {
        if (role.equals(Role.ADMIN)) {
            String imagePath = "file:src/main/resources/image/customer/login/Admin.gif";
            imageStatus.setImage(new Image(imagePath));
        } else if (role.equals(Role.NONE)) {
            String imagePath = "file:src/main/resources/image/customer/login/User.gif";
            imageStatus.setImage(new Image(imagePath));
        }
    }

}
