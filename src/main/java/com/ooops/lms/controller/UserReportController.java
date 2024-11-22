package com.ooops.lms.controller;

import com.ooops.lms.Alter.CustomerAlter;
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
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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
    private static final String REPORT_CARD_FXML ="/com/ooops/lms/library_management_system/UserReportCard-view.fxml";

    private FXMLLoaderUtil fxmlLoaderUtil = FXMLLoaderUtil.getInstance();

    @FXML
    private VBox reportsBox,vbox;
    @FXML
    private TextField reportTitleText;
    @FXML
    private TextArea reportContentText;

    private List<Report> reports = new ArrayList<>();
    private Report currentReport;
    private UserReportCardController userReportCardController;

    public void initialize() {
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("member_ID", UserMenuController.getMember().getPerson().getId());

        try {
            reports = ReportDAO.getInstance().searchByCriteria(criteria);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < reports.size(); i++) {
            loadReport(reports.get(i));
        }

        CustomerAlter.showMessage("Chú ý: nếu m report linh tinh thì t ban luôn");
    }

    public void onBackButtonAction(ActionEvent actionEvent) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(SETTING_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
        }
    }

    public void onAddReportButtonAction(ActionEvent actionEvent) {
        if(currentReport!=null && (currentReport.getContent().isEmpty()
                || currentReport.getTitle().isEmpty())) {
            CustomerAlter.showMessage("Viết xong rì pọt kia đi thì cho thêm");
            return;
        }
        currentReport = new Report(UserMenuController.getMember(),"","");
        currentReport.setStatus(ReportStatus.PENDING);
        try {
            //load new report
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(REPORT_CARD_FXML));
            HBox cardBox = fxmlLoader.load();
            userReportCardController = fxmlLoader.getController();
            userReportCardController.setData(currentReport);
            reportsBox.getChildren().add(cardBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //set data and css
        reportContentText.setText("");
        reportTitleText.setText("");
        vbox.setStyle("-fx-background-color: #FF7878;-fx-background-radius: 20;");
    }

    public void showIssueContent(Report report,UserReportCardController userReportCardController) {
        this.userReportCardController=userReportCardController;
        this.currentReport = report;
        reportContentText.setText(report.getContent());
        reportTitleText.setText(report.getTitle());
        String statusText = report.getStatus().toString();
        if(statusText.equals("PENDING")) {
            vbox.setStyle("-fx-background-color: #FF7878;-fx-background-radius: 20;");
        } else {
            vbox.setStyle("-fx-background-color: #AFFF84;-fx-background-radius: 20;");
        }
    }

    public void onSaveButtonAction(ActionEvent actionEvent) {
        if (currentReport != null) {
            if(reportContentText.getText().isEmpty()
            || reportTitleText.getText().isEmpty()) {
                CustomerAlter.showMessage("Hãy điền đủ report!");
                return;
            }
            if(reports.contains(currentReport)) {
                updateReport();
            } else {
                addReport();
            }
        } else {
            CustomerAlter.showMessage("Hãy chọn report để sửa hoặc tạo mới");
        }
    }

    private void loadReport(Report report) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(REPORT_CARD_FXML));
            HBox cardBox = fxmlLoader.load();
            UserReportCardController cardController = fxmlLoader.getController();
            cardController.setData(report);
            reportsBox.getChildren().add(cardBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onReportTitleTextMouseClicked(MouseEvent mouseEvent) {
        if(currentReport == null) {
            CustomerAlter.showMessage("hãy chọn một report hoặc tạo report để sửa");
        }
    }

    public void onReportContentTextMouseClicked(MouseEvent mouseEvent) {
        if(currentReport == null) {
            CustomerAlter.showMessage("hãy chọn một report hoặc tạo report để sửa");
        }
    }

    private void updateReport() {
        try {
            currentReport.setContent(reportContentText.getText());
            currentReport.setTitle(reportTitleText.getText());
            userReportCardController.editReport(currentReport);
            ReportDAO.getInstance().update(currentReport);
            CustomerAlter.showMessage("Mình lưu cho bạn rùi nhá :)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        reportContentText.setText("");
        reportTitleText.setText("");
        vbox.setStyle("-fx-background-color: #ffffff;-fx-background-radius: 20;");
        currentReport = null;
    }

    private void addReport() {
        try {
            currentReport.setContent(reportContentText.getText());
            currentReport.setTitle(reportTitleText.getText());
            userReportCardController.editReport(currentReport);
            ReportDAO.getInstance().add(currentReport);
            CustomerAlter.showMessage("Mình lưu cho bạn rùi nhá :)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        reportContentText.setText("");
        reportTitleText.setText("");
        vbox.setStyle("-fx-background-color: #ffffff;-fx-background-radius: 20;");
        currentReport = null;
    }

}
