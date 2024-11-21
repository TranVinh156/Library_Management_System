package com.ooops.lms.controller;

import com.ooops.lms.controller.BasePageController;
import com.ooops.lms.model.Member;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class AdminUserPageController extends BasePageController<Member, AdminUserDetailController, AdminUserTableController> {

    private static final String DETAIL_FXML = "/com/ooops/lms/library_management_system/AdminUserDetail.fxml";
    private static final String TABLE_FXML = "/com/ooops/lms/library_management_system/AdminUserTable.fxml";

    @FXML
    private AnchorPane messageLocationPane;

    @FXML
    private AnchorPane userDetailLocationPane;

    @FXML
    private AnchorPane userTableLocationPane;
    @FXML
    protected Label titlePage;

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
        userTableLocationPane.getChildren().add(tablePane);
        userDetailLocationPane.getChildren().add(detailPane);
        messageLocationPane.getChildren().add(messagePane);
    }
    @Override
    public void alterPage() {

    }
    @Override
    public  void startPage() {

    }
    @Override
    public void setTitlePage() {
        titlePage.setText("Quản lý độc giả");
    }
}
