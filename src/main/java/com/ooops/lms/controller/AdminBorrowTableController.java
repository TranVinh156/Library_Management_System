package com.ooops.lms.controller;

import com.ooops.lms.controller.BaseTableController;
import com.ooops.lms.database.dao.BookDAO;
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
        findCriteria.clear();
        if(!barCodeFindText.getText().isEmpty()){
            findCriteria.put("barcode",barCodeFindText.getText());
        }
        if(!bookNameFindText.getText().isEmpty()){
            findCriteria.put("bookName",bookNameFindText.getText());
        }
        if(!borrowDateFindText.getText().isEmpty()){
            findCriteria.put("creation_date",borrowDateFindText.getText());
        }
        if(!borrowerFindText.getText().isEmpty()){
            findCriteria.put("last_name",borrowerFindText.getText());
        }
        if(!borrowerFindText.getText().isEmpty()){
            findCriteria.put("first_name",borrowerFindText.getText());
        }
        if(!memeberIDFindText.getText().isEmpty()){
            findCriteria.put("memnberID",memeberIDFindText.getText());
        }
        if(!statusFindText.getText().isEmpty()){
            findCriteria.put("status",statusFindText.getText());
        }

    }

    @FXML
    void onAddButtonAction(ActionEvent event) {
        mainController.loadAddPane();
    }

    @Override
    protected void searchCriteria() {
        getCriteria();
        if(findCriteria.isEmpty()) {
            loadData();
            return;
        }
        try {
            itemsList.clear();
            itemsList.addAll(BookIssueDAO.getInstance().searchByCriteria(findCriteria));
        }catch (Exception e) {
            e.printStackTrace();
        }
        loadRows();
    }

    @FXML
    void onFindButtonAction(ActionEvent event) {
        searchCriteria();
    }

}
