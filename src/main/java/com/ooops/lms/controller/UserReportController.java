package com.ooops.lms.controller;

import com.ooops.lms.database.dao.ReportDAO;
import com.ooops.lms.model.Member;
import com.ooops.lms.model.Report;
import com.ooops.lms.model.enums.ReportStatus;
import com.ooops.lms.util.FXMLLoaderUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserReportController {
    private static final String SETTING_FXML = "/com/ooops/lms/library_management_system/Setting-view.fxml";

    private FXMLLoaderUtil fxmlLoaderUtil = FXMLLoaderUtil.getInstance();

    @FXML
    private VBox reportsBox;
    @FXML
    private TextField reportTitleText;
    @FXML
    private TextArea reportContentText;

    private List<Report> reports = new ArrayList<>();
    private Report currentReport;
    private UserReportCardController userReportCardController;

    public void initialize() {
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("member_ID", UserMenuController.member.getPerson().getId());

        try {
            reports = ReportDAO.getInstance().searchByCriteria(criteria);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < reports.size(); i++) {
            loadReport(reports.get(i));
        }
    }

    public void onBackButtonAction(ActionEvent actionEvent) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(SETTING_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
        }
    }

    public void onAddReportButtonAction(ActionEvent actionEvent) {
        currentReport = new Report(UserMenuController.member,"","");
        currentReport.setStatus(ReportStatus.PENDING);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/ooops/lms/library_management_system/UserReportCard-view.fxml"));
            HBox cardBox = fxmlLoader.load();
            userReportCardController = fxmlLoader.getController();
            userReportCardController.setData(currentReport);
            reportsBox.getChildren().add(cardBox);
            try {
                ReportDAO.getInstance().add(currentReport);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        reportContentText.setText("");
        reportTitleText.setText("");
    }

    public void showIssueContent(Report report,UserReportCardController userReportCardController) {
        this.userReportCardController=userReportCardController;
        this.currentReport = report;
        reportContentText.setText(report.getContent());
        reportTitleText.setText(report.getTitle());
    }

    public void onSaveButtonAction(ActionEvent actionEvent) {
        if (currentReport != null) {
            currentReport.setContent(reportContentText.getText());
            currentReport.setTitle(reportTitleText.getText());
            userReportCardController.editReport(currentReport);
            try {
                ReportDAO.getInstance().update(currentReport);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void loadReport(Report report) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/ooops/lms/library_management_system/UserReportCard-view.fxml"));
            HBox cardBox = fxmlLoader.load();
            UserReportCardController cardController = fxmlLoader.getController();
            cardController.setData(report);
            reportsBox.getChildren().add(cardBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
