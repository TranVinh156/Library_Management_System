package com.ooops.lms.controller;

import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.email.EmailUtil;
import com.ooops.lms.model.Admin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AdminMessageController extends BasicController {
    private EmailUtil emailUtil;

    @FXML
    private TextArea detailText;

    @FXML
    private Button sendButton;

    @FXML
    private TextField toText;

    @FXML
    private TextField topicText;

    @FXML
    void onSendButtonAction(ActionEvent event) {
        String toEmail = toText.getText();
        String topicEmail = topicText.getText();
        String detail = detailText.getText();
            if(checkEmail(toEmail,detail)) {
                emailUtil = new EmailUtil(toEmail, topicEmail, detail);
                //emailUtil.sendEmail();
                CustomerAlter.showMessage("Email đã được gửi tới " + toEmail);
            }

    }

    private boolean checkEmail(String toEmail,String detail) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (toEmail== null || !toEmail.matches(emailRegex)) {
            CustomerAlter.showMessage("Email không hợp lệ");
            return false;
        }

        if(detail == null || detail.isEmpty()) {
            CustomerAlter.showMessage("Email không có nội dung");
            return false;
        }

        return true;
    }
}
