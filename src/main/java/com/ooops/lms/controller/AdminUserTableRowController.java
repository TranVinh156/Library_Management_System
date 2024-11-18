package com.ooops.lms.controller;

import com.ooops.lms.controller.BaseRowController;
import com.ooops.lms.model.Member;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;


public class AdminUserTableRowController extends BaseRowController<Member, AdminUserPageController> {

    @FXML
    private HBox mainRowHbox;
    @FXML
    private Label memberIDlabel;

    @FXML
    private Label memberNameLabel;

    @FXML
    private Label phoneNumberLabel;

    @Override
    protected void updateRowDisplay() {
        memberIDlabel.setText(String.valueOf(item.getPerson().getId()));
        memberNameLabel.setText(item.getPerson().getFirstName() + " " + item.getPerson().getLastName());
        phoneNumberLabel.setText(item.getPerson().getPhone());
    }

}