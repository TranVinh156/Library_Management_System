package com.ooops.lms.controller;

import com.ooops.lms.model.Report;
import com.ooops.lms.model.enums.ReportStatus;
import javafx.event.ActionEvent;
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
    private static AdminIssuePageController mainController;
    private static AdminMenuController adminMenuController;

    public void setItem(Report report) {
        this.report = report;
        idMemberLabel.setText(report.getMember().getPerson().getId()+"");
        statusLabel.setText(report.getStatus().getDisplayName());
        titleLabel.setText(report.getTitle());
        if(report.getStatus().equals(ReportStatus.PENDING)) {
            statusLabel.setStyle("-fx-text-fill: red;");
        } else {
            statusLabel.setStyle("-fx-text-fill: black;");
        }
        setupRowClickHandler();
    }

    public void setMainController(AdminIssuePageController mainController) {
        this.mainController = mainController;
        if(mainController == null) {
            System.out.println("mainController is null");;
        }
    }
    public void setMenuController(AdminMenuController menuController) {
        this.adminMenuController = menuController;
        if(adminMenuController == null) {
            System.out.println("adminMenuController is null1");;
        }
    }
    private void setupRowClickHandler() {
        mainRowHbox.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                handleRowClick();
            }
        });
    }
    protected void handleRowClick() {
        if(adminMenuController == null) {
            System.out.println("adminMenuController is null2");;
        }
        this.adminMenuController.onIssuesButtonAction(new ActionEvent());
        mainController.loadDetail(this.report);
    }

}
