package com.ooops.lms.controller;

import com.ooops.lms.controller.BaseTableController;
import com.ooops.lms.database.dao.BookDAO;
import com.ooops.lms.model.Book;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.*;

public class AdminBookTableController extends BaseTableController<Book, AdminBookPageController, AdminBookTableRowController> {

    @FXML
    private TextField ISBNFindText;

    @FXML
    private Button addButton;

    @FXML
    private TextField authorFindText;

    @FXML
    private TextField bookNameFindTExt;

    @FXML
    private VBox tableVbox;

    @FXML
    private Button categoryFindButton;

    @FXML
    private VBox categoryList;

    @FXML
    private AnchorPane categoryTable;

    @FXML
    private Button findButton;

    @FXML
    private HBox mainPane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField stausText;

    @FXML
    private AnchorPane tableBookPane;

    @FXML
    private Label totalNumberBookLabel;

    @FXML
    private Label totalNumberBorrowLabel;

    @FXML
    private Label totalNumberIssueLabel;

    @FXML
    private Label totalNumberLostLabel;

    Map<String, Object> findCriteria = new HashMap<>();

    private static final String ROW_FXML = "/com/ooops/lms/library_management_system/AdminBookTableRow.fxml";

    @Override
    protected String getRowFXML() {
        return ROW_FXML;
    }

    @Override
    protected void loadDataFromSource() throws SQLException {
        itemsList.addAll(BookDAO.getInstance().selectAll());
    }

    @Override
    protected void getCriteria(){
        findCriteria.clear();

        // Kiểm tra và thêm tiêu chí tìm kiếm theo ISBN
        if (!ISBNFindText.getText().trim().isEmpty()) {
            findCriteria.put("ISBN", ISBNFindText.getText());
        }

        // Kiểm tra và thêm tiêu chí tìm kiếm theo tên sách
        if (!bookNameFindTExt.getText().trim().isEmpty()) {
            findCriteria.put("title", bookNameFindTExt.getText());
        }

        // Kiểm tra và thêm tiêu chí tìm kiếm theo tác giả
        if (!authorFindText.getText().trim().isEmpty()) {
            findCriteria.put("author", authorFindText.getText().trim());
        }

        // Kiểm tra và thêm tiêu chí tìm kiếm theo trạng thái
        if (!stausText.getText().trim().isEmpty()) {
            findCriteria.put("status", stausText.getText().trim().toUpperCase());
        }
    }

    @FXML
    void onCategoryFindButton(ActionEvent event) {
        categoryTable.setVisible(!categoryTable.isVisible());
        //updateCategoryTablePosition(mainPane, categoryFindButton, categoryTable);
    }

    @FXML
    void onFindButtonAction(ActionEvent event) {
        searchCriteria();
    }

    @FXML
    void onAddButtonAction(ActionEvent event) {
        mainController.loadAddPane();
    }

}
