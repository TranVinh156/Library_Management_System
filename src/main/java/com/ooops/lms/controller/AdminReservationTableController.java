package com.ooops.lms.controller;

import com.ooops.lms.database.dao.BookIssueDAO;
import com.ooops.lms.database.dao.BookReservationDAO;
import com.ooops.lms.model.BookIssue;
import com.ooops.lms.model.BookReservation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.sql.SQLException;

public class AdminReservationTableController extends BaseTableController<BookReservation, AdminReservationPageController,AdminReservationTableRowController> {

    private static final String ROW_FXML = "/com/ooops/lms/library_management_system/AdminReservationTableRow.fxml";

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

    @Override
    protected String getRowFXML() {
        return ROW_FXML;
    }

    @Override
    protected void loadDataFromSource() throws SQLException {
        itemsList.addAll(BookReservationDAO.getInstance().selectAll());
    }
    @Override
    protected void getCriteria(){
        findCriteria.clear();
        if(!barCodeFindText.getText().isEmpty()){
            findCriteria.put("barCode",barCodeFindText.getText());
        }
        if(!bookNameFindText.getText().isEmpty()){
            findCriteria.put("bookName",bookNameFindText.getText());
        }
        if(!borrowDateFindText.getText().isEmpty()){
            findCriteria.put("borrowDate",borrowDateFindText.getText());
        }
        if(!borrowerFindText.getText().isEmpty()){
            findCriteria.put("borrower",borrowerFindText.getText());
        }
        if(!memeberIDFindText.getText().isEmpty()){
            findCriteria.put("memeberID",memeberIDFindText.getText());
        }
        if(!statusFindText.getText().isEmpty()){
            findCriteria.put("status",statusFindText.getText());
        }
    }

    @FXML
    void onAddButtonAction(ActionEvent event) {
        mainController.loadAddPane();
    }

    @FXML
    void onFindButtonAction(ActionEvent event) {

    }

}
