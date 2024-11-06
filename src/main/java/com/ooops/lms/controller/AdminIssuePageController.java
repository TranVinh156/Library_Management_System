package com.ooops.lms.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class AdminIssuePageController extends BasicIssueController {


    @FXML
    private AnchorPane DetailPane;

    @FXML
    private HBox detailPage;

    @FXML
    private AnchorPane messangePane;

    @FXML
    private Button returnButton;

    @FXML
    private AnchorPane tablePage;

    @FXML
    void onReturnButtonAction(ActionEvent event) {
        System.out.println("eeee");
        alterPage();
    }

    private AdminIssueDetailController adminIssueDetailController;
    private AdminIssueTableController adminIssueTableController;

    @FXML
    public void initialize() throws IOException {


        tablePage.getChildren().add(issueTablePane);
        adminIssueTableController = issueTablePaneLoader.getController();
        adminIssueTableController.setMainController(this);

        DetailPane.getChildren().add(issueDetailPane);
        adminIssueDetailController = issueDetailPaneLoader.getController();
        adminIssueDetailController.setMainController(this);

        FXMLLoader loader = new FXMLLoader(getClass().getResource(MESSAGE_FXML));
        Node mess = loader.load();
        AnchorPane.setTopAnchor((AnchorPane)mess, 0.0);
        AnchorPane.setBottomAnchor((AnchorPane)mess, 0.0);
        AnchorPane.setLeftAnchor((AnchorPane)mess, 0.0);
        AnchorPane.setRightAnchor((AnchorPane)mess, 0.0);
        this.messangePane.getChildren().add(mess);

    }

    public void loadDetail() {
        System.out.println("loadDetail");
        // adminIssueDetailController.loadStartStatus();
        alterPage();
        //adminIssueDetailController.setItem();
    }

    public void alterPage() {
        System.out.println("alterPage");
        detailPage.setVisible(!detailPage.isVisible());
        tablePage.setVisible(!tablePage.isVisible());
    }
}