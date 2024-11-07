package com.ooops.lms.controller;

import com.ooops.lms.model.Book;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class AdminBookPageController extends BasicBookController {

    @FXML
    private AnchorPane detailLocation;

    @FXML
    private AnchorPane detailPage;

    @FXML
    private Button returnButton;

    @FXML
    private AnchorPane tablePage;

    private AdminBookDetailController adminBookDetailController;
    private AdminBookTableController adminBookTableController;


    @FXML
    public void initialize() {
        adminBookDetailController = bookDetailPaneLoader.getController();
        adminBookDetailController.setMainController(this);
        adminBookTableController = bookTablePaneLoader.getController();
        adminBookTableController.setMainController(this);
        tablePage.getChildren().add(bookTablePane);
        detailLocation.getChildren().add(bookDetailPane);

    }

    @FXML
    void onReturnButton(ActionEvent event) {
        alterPage();
    }

    public void loadDetail(Book book) {
        System.out.println("load Detail");
        alterPage();
    }

    public void alterPage() {
        detailPage.setVisible(!detailPage.isVisible());
        tablePage.setVisible(!tablePage.isVisible());
    }

}
