package com.ooops.lms.controller;

import com.ooops.lms.database.dao.BookIssueDAO;
import com.ooops.lms.database.dao.BookReservationDAO;
import com.ooops.lms.model.BookIssue;
import com.ooops.lms.model.BookReservation;
import com.ooops.lms.model.enums.BookReservationStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AdminReservationTableController extends BaseTableController<BookReservation, AdminReservationPageController, AdminReservationTableRowController> {

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
    private ChoiceBox<String> statusFindBox;

    @FXML
    private Button findButton;

    @FXML
    private VBox tableVbox;

    @FXML
    protected void initialize() {
        statusFindBox.getItems().add("None");
        statusFindBox.getItems().addAll(BookReservationStatus.CANCELED.toString(), BookReservationStatus.WAITING.toString(), BookReservationStatus.COMPLETED.toString());
    }

    @Override
    protected String getRowFXML() {
        return ROW_FXML;
    }

    @Override
    protected void loadDataFromSource() throws SQLException {
        itemsList.addAll(BookReservationDAO.getInstance().selectAll());
    }

    @Override
    protected void getCriteria() {
        findCriteria.clear();
        if (!barCodeFindText.getText().isEmpty()) {
            findCriteria.put("barcode", barCodeFindText.getText());
        }
        if(!bookNameFindText.getText().isEmpty()){
            findCriteria.put("title",bookNameFindText.getText());
        }
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // hoặc định dạng phù hợp với dữ liệu của bạn
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        // Chuyển đổi và định dạng cho borrowDate
        if (borrowDateFindText.getText() != null && !borrowDateFindText.getText().isEmpty()) {
            LocalDate createdDate = LocalDate.parse(borrowDateFindText.getText(), inputFormatter);
            createdDate.format(outputFormatter);
            findCriteria.put("creation_date", createdDate.toString());
        }

        if (!borrowerFindText.getText().isEmpty()) {
            findCriteria.put("fullname", borrowerFindText.getText());
        }
        if (!memeberIDFindText.getText().isEmpty()) {
            findCriteria.put("member_ID", memeberIDFindText.getText());
        }
        if (!statusFindBox.getItems().isEmpty() && statusFindBox.getValue() != "None" && statusFindBox.getValue() != null) {
            findCriteria.put("BookReservationStatus", statusFindBox.getValue());
        }

    }

    @FXML
    void onAddButtonAction(ActionEvent event) {
        mainController.loadAddPane();
    }

    @FXML
    protected void searchCriteria() {
        getCriteria();
        try {
            itemsList.clear();
            itemsList.addAll(BookReservationDAO.getInstance().searchByCriteria(findCriteria));
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadRows();
    }

    @FXML
    void onFindButtonAction(ActionEvent event) {
        searchCriteria();
    }

}
