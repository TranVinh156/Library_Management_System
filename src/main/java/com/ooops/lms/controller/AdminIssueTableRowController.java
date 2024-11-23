package com.ooops.lms.controller;

import com.ooops.lms.controller.BaseRowController;
import com.ooops.lms.model.Report;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class AdminIssueTableRowController extends BaseRowController<Report, AdminIssuePageController> {

    @FXML
    private Label detailLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label memberIDLabel;

    @FXML
    private Label memberNameLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private HBox mainRowHbox;

    @Override
    protected void updateRowDisplay() {
        memberIDLabel.setText(item.getMember().getPerson().getId() + "");
        memberNameLabel.setText(item.getMember().getPerson().getFirstName() + " " + item.getMember().getPerson().getLastName());
        titleLabel.setText(item.getTitle());
        detailLabel.setText(item.getContent());
        emailLabel.setText(item.getMember().getPerson().getEmail());
        statusLabel.setText(item.getStatus().toString());
        if(statusLabel.getText().equals("RESOLVED")){
            statusLabel.setStyle("-fx-text-fill: green;");
        } else if(statusLabel.getText().equals("PENDING")){
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }


}
