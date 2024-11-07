package com.ooops.lms.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminIssueTableController extends BasicIssueController {
    @FXML
    private TextField emailFindText;

    @FXML
    private Button findButton;

    @FXML
    private VBox issueTableVbox;

    @FXML
    private TextField memberNameFindText;

    @FXML
    private TextField memeberIDFindText;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField statusFindText;

    @FXML
    private AnchorPane mainPane;

    private AdminIssuePageController mainController;
    private List<Node> issueList = new ArrayList<>();

    void setMainController(AdminIssuePageController mainController) {
        this.mainController = mainController;
        insertRowTest();
    }

    @FXML
    public void initialize() {
        childFitHeightParent(issueTableVbox,scrollPane);
        childFitWidthParent(issueTableVbox,scrollPane);
    }

    @FXML
    void onFindButtonAction(ActionEvent event) {

    }

    private void loadRows(){
        for (int i = 0; i < issueList.size(); i++) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(ISSUE_TABLE_ROW_FXML));
                Node row = loader.load();

                // AdminIssueTableRowController rowController = loader.getController();

                // Thiết lập controller của bảng cho hàng
                // rowController.setTableController(this); // Set controller của table cho row
                //rowController.setItem();
                childFitWidthParent(row, scrollPane);
                issueTableVbox.getChildren().add(row);
            } catch (IOException e) {

            }
        }
    }


    private void insertRowTest() {
        for (int i = 0; i < 20; i++) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(ISSUE_TABLE_ROW_FXML));
                Node row = loader.load();

                AdminIssueTableRowController rowController = loader.getController();

                // Thiết lập controller của bảng cho hàng
                rowController.setTableController(mainController); // Set controller của table cho row
                //rowController.setItem();
                childFitWidthParent(row, issueTableVbox);
                issueList.add(row);
                issueTableVbox.getChildren().add(row);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
