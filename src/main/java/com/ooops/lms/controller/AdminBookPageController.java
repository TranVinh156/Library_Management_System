package com.ooops.lms.controller;

import com.ooops.lms.controller.BasePageController;
import com.ooops.lms.model.Book;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class AdminBookPageController extends BasePageController<Book, AdminBookDetailController, AdminBookTableController> {

    private static final String TABLE_FXML = "/com/ooops/lms/library_management_system/AdminBookTable.fxml";
    private static final String DETAIL_FXML = "/com/ooops/lms/library_management_system/AdminBookDetail.fxml";

    @FXML
    private AnchorPane detailLocation;

    @FXML
    private AnchorPane detailPage;

    @FXML
    private Button returnButton;

    @FXML
    private AnchorPane tablePage;

    @FXML
    private Label titlePage;

    private AdminDashboardController adminDashboardController;

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
        adminDashboardController = dashboardLoader.getController();
        adminDashboardController.setAdminBookPageController(this);

    }

    @Override
    protected void setupViews() {
        tablePage.getChildren().add(tablePane);
        detailLocation.getChildren().add(detailPane);
    }

    /**
     * Khi mà bấm quay lại (page1 -> page2) thì sẽ load lại Data của bảng Table và các Summary Total
     *
     * @param event
     */
    @FXML
    void onReturnButton(ActionEvent event) {
        while (getTitlePageStack().peek() != "Quản lý sách") {
            getTitlePageStack().pop();
        }
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
