package com.ooops.lms.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class AdminIssueTableRowController extends BasicIssueController {

    @FXML
    private Label detailLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label memberIDLabel;

    @FXML
    private Label memberNameLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private HBox mainRowHbox;

    private AdminIssuePageController mainController = null;

    void setTableController(AdminIssuePageController mainController) {
        this.mainController = mainController;
    }

    public void initialize()  {
        mainRowHbox.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                handleClickRow();
            }
        });

    }

    public void handleClickRow() {
        System.out.println("Click CLick");
        mainController.loadDetail();
    }


    public void setItem(Object item) {

    }


}
