package com.ooops.lms.controller;

import com.ooops.lms.controller.BaseTableController;
import com.ooops.lms.database.dao.ReportDAO;
import com.ooops.lms.model.Report;
import com.ooops.lms.model.enums.ReportStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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
    private ChoiceBox<String> statusFindBox;

    @FXML
    private VBox tableVbox;
    private AdminDashboardController adminDashboardController;

    public void initialize() {
        statusFindBox.getItems().add("None");
        statusFindBox.getItems().addAll(ReportStatus.PENDING.toString(),ReportStatus.RESOLVED.toString());
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
        findCriteria.put("ReportStatus","PENDING");
        int totalIssuel = ReportDAO.getInstance().searchByCriteria(findCriteria).size();
        adminDashboardController.setTotalIssueLabel(totalIssuel+"");
        findCriteria.clear();
    }

    @FXML
    void onFindButtonAction(ActionEvent event) {
        searchCriteria();
    }

    @Override
    protected void getCriteria(){
        findCriteria.clear();
        if(!memeberIDFindText.getText().isEmpty()){
            findCriteria.put("member_ID",memeberIDFindText.getText());
        }
        if (!statusFindBox.getItems().isEmpty() && statusFindBox.getValue() != "None" && statusFindBox.getValue() != null) {
            findCriteria.put("ReportStatus", statusFindBox.getValue());
        }

    }


}
