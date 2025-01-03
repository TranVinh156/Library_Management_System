package com.ooops.lms.controller;

import com.ooops.lms.database.dao.ReportDAO;
import com.ooops.lms.model.Report;
import com.ooops.lms.model.enums.ReportStatus;
import com.ooops.lms.util.FXMLLoaderUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.io.IOException;


public class UserReportCardController {
    @FXML
    private Label reportTitleText, statusText;

    @FXML
    HBox reportCardBox;

    private Report report;

    private static final String USER_REPORT_FXML = "/com/ooops/lms/library_management_system/UserReport-view.fxml";

    public void setData(Report report) {
        this.report =report;
        reportTitleText.setText(report.getTitle());
        statusText.setText(report.getStatus().toString());
        if(statusText.getText() == "PENDING") {
            reportCardBox.setStyle("-fx-background-color: #FF7878;-fx-background-radius: 20;");
        } else {
            reportCardBox.setStyle("-fx-background-color: #AFFF84;-fx-background-radius: 20;");
        }
    }

    public void editReport(Report report) {
        reportTitleText.setText(report.getTitle());
        statusText.setText(report.getStatus().toString());
    }

    public void onShowReportMouseClicked(MouseEvent mouseEvent) {
        try {
            UserReportController userReportController = FXMLLoaderUtil.getInstance().getController(USER_REPORT_FXML);
            userReportController.showIssueContent(report,this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
