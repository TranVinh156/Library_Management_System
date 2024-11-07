package com.ooops.lms.controller;

import com.google.api.services.books.v1.Books;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminBorrowTableController extends BasicBorrowController {

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

    private AdminBorrowPageController mainController;
    private List<Node> borrowList = new ArrayList<>();

    @FXML
    void onAddButtonAction(ActionEvent event) {
        mainController.addTable();
    }

    @FXML
    void onFindButtonAction(ActionEvent event) {

    }

    void setMainController(AdminBorrowPageController mainController) {
        this.mainController = mainController;
        insertRowTest();
        childFitHeightParent(tableVbox,scrollPane);
        childFitWidthParent(tableVbox,scrollPane);
    }

    private void insertRowTest() {
        for (int i = 0; i < 20; i++) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(BORROW_TABLE_ROW_FXML));
                Node row = loader.load();

                AdminBorrowTableRowController rowController = loader.getController();

                // Thiết lập controller của bảng cho hàng
                rowController.setTableController(mainController); // Set controller của table cho row
                //rowController.setItem();
                childFitWidthParent(row, scrollPane);
                borrowList.add(row);
                tableVbox.getChildren().add(row);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
