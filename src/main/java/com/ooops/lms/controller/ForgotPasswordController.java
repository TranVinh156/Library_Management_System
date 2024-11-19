package com.ooops.lms.controller;

import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.database.dao.MemberDAO;
import com.ooops.lms.model.Member;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<String, Object> findCriteria = new HashMap<>();
        findCriteria.put("email", emailText.getText());
        List<Member> resultList = new ArrayList<>();
        try {
            resultList = MemberDAO.getInstance().searchByCriteria(findCriteria);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(resultList.isEmpty() || resultList.size() < 1) {
            System.out.println(resultList.size());
            return false;
        }
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
        openLoginView();
    }

    public void onToEmailButtonAction(ActionEvent actionEvent) {
        isStep1 = !isStep1;
        enterEmailHBox.setVisible(true);
        enterVerifyHBox.setVisible(false);
    }
}
