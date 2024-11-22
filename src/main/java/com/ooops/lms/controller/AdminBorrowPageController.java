package com.ooops.lms.controller;

import com.ooops.lms.controller.BasePageController;
import com.ooops.lms.model.BookIssue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class AdminBorrowPageController extends BasePageController<BookIssue, AdminBorrowDetailController, AdminBorrowTableController> {

    private static final String TABLE_FXML = "/com/ooops/lms/library_management_system/AdminBorrowTable.fxml";
    private static final String DETAIL_FXML = "/com/ooops/lms/library_management_system/AdminBorrowDetail.fxml";

    @FXML
    private AnchorPane detailLocation;

    @FXML
    private AnchorPane detailPage;

    @FXML
    private AnchorPane tablePage;

    @FXML
    private Button returnButton;

    @FXML
    private Label titlePage;

    @Override
    protected String getDetailFXMLPath() {
        return DETAIL_FXML;
    }

    @Override
    protected String getTableFXMLPath() {
        return TABLE_FXML;
    }

    @Override
    protected void setupControllers() {

    }
    @Override
    protected void setupViews() {
        this.tablePage.getChildren().add(super.tablePane);
        this.detailLocation.getChildren().add(super.detailPane);
    }

    @FXML
    void onReturnButtonAction(ActionEvent event) {
        getTitlePageStack().pop();
        loadData();
        alterPage();
    }
    @Override
    public void alterPage() {
        detailPage.setVisible(!detailPage.isVisible());
        tablePage.setVisible(!tablePage.isVisible());
        if(detailPage.isVisible()) {
            page1 = false;
        } else {
            page1 = true;
        }
    }
    @Override
    public void startPage() {
        page1 = true;
        setTitlePage();
        detailPage.setVisible(false);
        tablePage.setVisible(true);
        loadData();
    }



}
