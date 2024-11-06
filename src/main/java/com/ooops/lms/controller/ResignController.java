package com.ooops.lms.controller;

import com.google.api.services.books.v1.model.Volume;
import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.database.dao.AccountDAO;
import com.ooops.lms.model.datatype.Person;
import com.ooops.lms.model.enums.Gender;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;

import static com.almasb.fxgl.app.GameApplication.launch;

public class ResignController extends BasicController {

    @FXML
    private Button backStepButton;

    @FXML
    private DatePicker birthDate;

    @FXML
    private Button continueButton;

    @FXML
    private TextField emailText;

    @FXML
    private TextField firstNameText;

    @FXML
    private ChoiceBox<Gender> genderBox;

    @FXML
    private TextField lastNameText;


    @FXML
    private PasswordField passwordText;

    @FXML
    private TextField phoneNumberText;

    @FXML
    private PasswordField rePasswordText;

    @FXML
    private Button resignButton;

    @FXML
    private AnchorPane resignStep1Pane;

    @FXML
    private AnchorPane resignStep2Pane;

    @FXML
    private Button returnLoginButton;


    @FXML
    private AnchorPane switchBar;

    @FXML
    private TextField usernameText;

    private boolean isStep1;
    private AccountDAO accountDAO = new AccountDAO();

    @FXML
    public void initialize() {
        isStep1 = true;
        setSwitchBar();
        genderBox.getItems().addAll(Gender.Male, Gender.Female, Gender.Other);
    }

    @FXML
    void onBackStepButtonOn(ActionEvent event) {
        isStep1 = !isStep1;
        switchToStep1();
    }

    @FXML
    void onContinueButtonAction(ActionEvent event) {
        if(checkInformation()) {
            isStep1 = !isStep1;
            switchToStep2();
        }
    }

    @FXML
    private void onResignButtonAction(ActionEvent event) {
        try {
            if (checkInformation()) {

                Person person = createPersonFromInput();
                String username = usernameText.getText();
                String password = passwordText.getText();


                if(accountDAO.registerMember(person, username, password)) {
                    CustomerAlter.showMessage("Đăng ký tài khoản thành công.");
                    openLoginView();
                }
            } else {
                CustomerAlter.showMessage("Vui lòng kiểm tra thông tin đã nhập.");
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    @FXML
    void onReturnLoginButtonAction(ActionEvent event) {
        openLoginView();
    }

    private boolean checkInformation() {
        if(isStep1) {
            // Bước 1: Kiểm tra thông tin cá nhân
            String firstName = firstNameText.getText().trim();
            String lastName = lastNameText.getText().trim();
            String birth = birthDate.getValue() != null ? birthDate.getValue().toString() : ""; // Kiểm tra ngày sinh
            Gender gender = genderBox.getSelectionModel().getSelectedItem();
            String email = emailText.getText().trim();

            // Kiểm tra tên
            if (firstName.isEmpty()) {
                CustomerAlter.showMessage("Tên không được để trống.");
                return false;
            }

            if (lastName.isEmpty()) {
                CustomerAlter.showMessage("Họ không được để trống.");
                return false;
            }

            // Kiểm tra ngày sinh
            if (birth.isEmpty()) {
                CustomerAlter.showMessage( "Ngày sinh không được để trống.");
                return false;
            }

            // Kiểm tra giới tính
            if (gender == null) {
                CustomerAlter.showMessage("Vui lòng chọn giới tính.");
                return false;
            }

            // Kiểm tra email
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) { // Biểu thức chính quy cho email
                CustomerAlter.showMessage("Email không hợp lệ.");
                return false;
            }

            return true;

        } else {
            // Bước 2: Kiểm tra thông tin tài khoản
            String username = usernameText.getText().trim();
            String phoneNumber = phoneNumberText.getText().trim();
            String password = passwordText.getText().trim();
            String rePassword = rePasswordText.getText().trim();

            // Kiểm tra tên người dùng
            if (username.isEmpty()) {
                CustomerAlter.showMessage("Tên người dùng không được để trống.");
                return false;
            }

            // Kiểm tra số điện thoại
            if (phoneNumber.isEmpty() || !phoneNumber.matches("\\d{10}")) { // Giả sử số điện thoại có 10 chữ số
                CustomerAlter.showMessage("Số điện thoại không hợp lệ (cần có 10 chữ số).");
                return false;
            }

            // Kiểm tra mật khẩu
            if (password.isEmpty()) {
                CustomerAlter.showMessage("Mật khẩu không được để trống.");
                return false;
            }

            // Kiểm tra xác nhận mật khẩu
            if (!password.equals(rePassword)) {
                CustomerAlter.showMessage ("Mật khẩu và xác nhận mật khẩu không khớp.");
                return false;
            }
            return true;
        }

    }

    private Person createPersonFromInput() {
        Person person = new Person();
        person.setFirstName(firstNameText.getText());
        person.setLastName(lastNameText.getText());
        person.setDateOfBirth(birthDate.getValue().toString());
        person.setGender(genderBox.getValue());
        person.setEmail(emailText.getText());
        person.setPhone(phoneNumberText.getText());
        return person;
    }

    private void handleSQLException(SQLException e) {
        String message = e.getMessage().equals("User already exists") ?
                "Tài khoản đã tồn tại. Vui lòng chọn tên đăng nhập khác." :
                "Đã xảy ra lỗi: " + e.getMessage();
        CustomerAlter.showMessage(message);
    }

    private void switchToStep1() {
        resignStep1Pane.setVisible(true);
        resignStep2Pane.setVisible(false);
        backStepButton.setVisible(false);
        setSwitchBar();
    }

    private void switchToStep2() {
        resignStep1Pane.setVisible(false);
        resignStep2Pane.setVisible(true);
        backStepButton.setVisible(true);
        setSwitchBar();
    }

    private void setSwitchBar() {
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(switchBar);
        transition.setDuration(Duration.seconds(0.5));
        transition.setToX(isStep1 ? 0 : 46); // Khoảng di chuyển dựa trên trạng thái
        transition.play();
    }

    private void openLoginView() {
        try {
            Stage stage = (Stage) resignButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/com/ooops/lms/library_management_system/UserLogin.fxml"));
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
