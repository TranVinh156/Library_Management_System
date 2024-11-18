package com.ooops.lms.controller;

import com.ooops.lms.controller.BasePageController;
import com.ooops.lms.model.Book;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
        loadData();
        alterPage();
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

}
