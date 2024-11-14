package com.ooops.lms.controller;

import com.ooops.lms.Alter.CustomerAlter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class ForgotPasswordController extends BasicController {

    @FXML
    private Button continueButton;

    @FXML
    private TextField emailText;

    @FXML
    private HBox enterEmailHBox;

    @FXML
    private HBox enterVerifyHBox;

    @FXML
    private TextField otpText;

    @FXML
    private Button sendAgainButton;

    @FXML
    private Button verifyButton;

    @FXML
    private Button returnLoginButton;

    private boolean isStep1;

    public void initialize() {
        isStep1 = true;
    }

    @FXML
    void onReturnLoginButtonAction(ActionEvent event) {
        openLoginView();
    }

    @FXML
    void onContinueButtonAction(ActionEvent event) {
        if(checkEmail()) {
            isStep1 = !isStep1;
            enterEmailHBox.setVisible(false);
            enterVerifyHBox.setVisible(true);
        } else {
            System.out.println("Email is not correct");
            CustomerAlter.showMessage("Email is not correct");
        }
    }

    @FXML
    void onSendAgainButton(ActionEvent event) {

    }

    @FXML
    void onVerifyButtonAction(ActionEvent event) {
        if(checkOTP()) {
            System.out.println("OTP is correct");
            CustomerAlter.showMessage("OTP is correct");
            openLoginView();
        }
    }

    private boolean checkEmail() {
        String email = emailText.getText();
        return true;

    }

    private boolean checkOTP() {
        String otp = otpText.getText();
        return true;
    }

    private void openLoginView() {
        try {
            Stage stage = (Stage) verifyButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/com/ooops/lms/library_management_system/UserLogin.fxml"));
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onToLoginButtonAction(ActionEvent actionEvent) {
    }

    public void onToEmailButtonAction(ActionEvent actionEvent) {
    }
}
