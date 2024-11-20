package com.ooops.lms.controller;

import com.ooops.lms.controller.BaseTableController;
import com.ooops.lms.database.dao.BookIssueDAO;
import com.ooops.lms.database.dao.ReportDAO;
import com.ooops.lms.model.BookIssue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.sql.SQLException;

public class AdminBorrowTableController extends BaseTableController<BookIssue, AdminBorrowPageController,AdminBorrowTableRowController> {

    private static final String ROW_FXML = "/com/ooops/lms/library_management_system/AdminBorrowTableRow.fxml";

    @FXML
    private Button addButton;

    @FXML
    private TextField barCodeFindText;

    @FXML
    private TextField bookNameFindText;

    @FXML
    private TextField borrowDateFindText;

    @FXML
    private TextField borrowerFindText;

    @FXML
    private TextField memeberIDFindText;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField statusFindText;

    @FXML
    private Button findButton;

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
        itemsList.addAll(BookIssueDAO.getInstance().selectAll());
        //Xu ly set total cho Dashboard
        findCriteria.clear();
        findCriteria.put("status","BORROWED");
        int totalBorrow = ReportDAO.getInstance().searchByCriteria(findCriteria).size();
        adminDashboardController.setTotalBorrowLabel(totalBorrow+"");
        findCriteria.clear();
    }
    @Override
    protected void getCriteria(){

    }

    @FXML
    void onAddButtonAction(ActionEvent event) {
        mainController.loadAddPane();
    }

    @FXML
    void onFindButtonAction(ActionEvent event) {

    }

}
