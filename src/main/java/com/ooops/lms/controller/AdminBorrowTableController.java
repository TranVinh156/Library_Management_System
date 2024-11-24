package com.ooops.lms.controller;

import com.ooops.lms.controller.BaseTableController;
import com.ooops.lms.database.dao.BookDAO;
import com.ooops.lms.database.dao.BookIssueDAO;
import com.ooops.lms.database.dao.BookItemDAO;
import com.ooops.lms.database.dao.ReportDAO;
import com.ooops.lms.model.BookIssue;
import com.ooops.lms.model.enums.BookIssueStatus;
import com.ooops.lms.model.enums.BookItemStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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
    private ChoiceBox<String> statusFindBox;

    @FXML
    private VBox tableVbox;

    private AdminDashboardController adminDashboardController;

    public void initialize() {
        statusFindBox.getItems().add("None");
        statusFindBox.getItems().addAll(BookIssueStatus.BORROWED.toString(), BookIssueStatus.RETURNED.toString(), BookIssueStatus.LOST.toString());
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
        findCriteria.put("BookItemStatus", BookItemStatus.LOANED.toString());
        int totalBorrow = BookItemDAO.getInstance().searchByCriteria(findCriteria).size();
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
            findCriteria.put("title",bookNameFindText.getText());
        }
        if(!borrowDateFindText.getText().isEmpty()){
            findCriteria.put("creation_date",borrowDateFindText.getText());
        }
        if(!borrowerFindText.getText().isEmpty()){
            findCriteria.put("fullname",borrowerFindText.getText());
        }

        if(!memeberIDFindText.getText().isEmpty()){
            findCriteria.put("member_ID",memeberIDFindText.getText());
        }
        if(statusFindBox.getValue() != "None" && statusFindBox.getValue() != null){
            findCriteria.put("BookIssueStatus",statusFindBox.getValue());
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
