package com.ooops.lms.controller;

import com.ooops.lms.model.Report;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class AdminRecentIssueController {

    @FXML
    private Label idMemberLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Label titleLabel;
    @FXML
    private HBox mainRowHbox;

    private Report report;
    private AdminIssuePageController mainController;

    public void setItem(Report report) {
        this.report = report;
        idMemberLabel.setText(report.getMember().getPerson().getId()+"");
        statusLabel.setText(report.getStatus().getDisplayName());
        titleLabel.setText(report.getTitle());
        setupRowClickHandler();
    }

    public void setMainController(AdminIssuePageController mainController) {
        this.mainController = mainController;
    }
    private void setupRowClickHandler() {
        mainRowHbox.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                handleRowClick();
            }
        });
    }
    protected void handleRowClick() {
        mainController.loadDetail(this.report);
    }

}
