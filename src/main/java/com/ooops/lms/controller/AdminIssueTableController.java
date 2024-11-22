package com.ooops.lms.controller;

import com.ooops.lms.controller.BaseTableController;
import com.ooops.lms.database.dao.ReportDAO;
import com.ooops.lms.model.Report;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.sql.SQLException;

public class AdminIssueTableController extends BaseTableController<Report, AdminIssuePageController, AdminIssueTableRowController> {
    protected static final String ROW_FXML = "/com/ooops/lms/library_management_system/AdminIssueTableRow.fxml";

    @FXML
    private TextField emailFindText;

    @FXML
    private Button findButton;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private TextField memberNameFindText;

    @FXML
    private TextField memeberIDFindText;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField statusFindText;

    @FXML
    private VBox tableVbox;
    private AdminDashboardController adminDashboardController;

    public void initialize() {
        adminDashboardController = dashboardLoader.getController();
    }

    @Override
    protected String getRowFXML() {
        return ROW_FXML;
    }

    @Override
    protected void loadDataFromSource() throws SQLException {
        itemsList.addAll(ReportDAO.getInstance().selectAll());

        //Xử lý set total cho Dashboard
        findCriteria.clear();
        findCriteria.put("status","PENDING");
        int totalIssuel = ReportDAO.getInstance().searchByCriteria(findCriteria).size();
        adminDashboardController.setTotalIssueLabel(totalIssuel+"");
        findCriteria.clear();
    }

    @FXML
    void onFindButtonAction(ActionEvent event) {

    }

    @Override
    protected void getCriteria(){

    }


}
