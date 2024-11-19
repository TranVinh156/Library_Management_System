package com.ooops.lms.controller;

import com.ooops.lms.controller.BasePageController;
import com.ooops.lms.model.Report;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class AdminIssuePageController extends BasePageController<Report, AdminIssueDetailController, AdminIssueTableController> {
    private static final String TABLE_FXML = "/com/ooops/lms/library_management_system/AdminIssueTable.fxml";
    private static final String DETAIL_FXML = "/com/ooops/lms/library_management_system/AdminIssueDetail.fxml";
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

    @Override
    protected String getDetailFXMLPath() {
        return DETAIL_FXML;
    }

    @Override
    protected String getTableFXMLPath() {
        System.out.println("Issuel");
        return TABLE_FXML;
    }

    @Override
    protected void setupControllers() {

    }

    @Override
    protected void setupViews() {
        this.tablePage.getChildren().add(super.tablePane);
        this.DetailPane.getChildren().add(super.detailPane);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(MESSAGE_FXML));
            Node mess = loader.load();
            AnchorPane.setTopAnchor((AnchorPane) mess, 0.0);
            AnchorPane.setBottomAnchor((AnchorPane) mess, 0.0);
            AnchorPane.setLeftAnchor((AnchorPane) mess, 0.0);
            AnchorPane.setRightAnchor((AnchorPane) mess, 0.0);
            this.messangePane.getChildren().add(mess);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void alterPage() {
        detailPage.setVisible(!detailPage.isVisible());
        tablePage.setVisible(!tablePage.isVisible());
    }

    @Override
    public void startPage() {
        detailPage.setVisible(false);
        tablePage.setVisible(true);
    }

    @FXML
    void onReturnButtonAction(ActionEvent event) {
        loadData();
        alterPage();
    }
}