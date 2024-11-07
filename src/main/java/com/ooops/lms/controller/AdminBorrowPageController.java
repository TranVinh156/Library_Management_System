package com.ooops.lms.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class AdminBorrowPageController extends BasicBorrowController {

    @FXML
    private AnchorPane detailLocation;

    @FXML
    private AnchorPane detailPage;

    @FXML
    private AnchorPane tablePage;

    @FXML
    private Button returnButton;

    private AdminBorrowDetailController adminBorrowDetailController;
    private AdminBorrowTableController adminBorrowTableController;

    @FXML
    void onReturnButtonAction(ActionEvent event) {
        alterPage();
    }

    public void initialize() {
        tablePage.getChildren().add(borrowTablePane);
        detailLocation.getChildren().add(borrowDetailPane);
        adminBorrowDetailController = borrowDetailPaneLoader.getController();
        adminBorrowTableController = borrowTablePaneLoader.getController();
        adminBorrowTableController.setMainController(this);
        adminBorrowDetailController.setMainController(this);
    }

    public void loadDetail() {
        alterPage();
    }

    public void addTable() {
        adminBorrowDetailController.setAddMode(true);
        alterPage();
    }

    public void alterPage() {
        detailPage.setVisible(!detailPage.isVisible());
        tablePage.setVisible(!tablePage.isVisible());
    }



}
