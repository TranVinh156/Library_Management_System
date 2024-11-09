package com.ooops.lms.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class BasicUserController extends BasicController {
    private static final String USER_DETAIL_FXML = "/com/ooops/lms/library_management_system/AdminUserDetail.fxml";
    protected static final String USER_TABLE_ROW_FXML = "/com/ooops/lms/library_management_system/AdminUserTableRow.fxml";
    private static final String USER_TABLE_FXML = "/com/ooops/lms/library_management_system/AdminUserTable.fxml";

    protected static final FXMLLoader userTablePaneLoader;
    protected static final FXMLLoader userDetailPaneLoader;
    protected static final Node userDetailPane;
    protected static final Node userTablePane;

    static {
        userTablePaneLoader = loadFXML(USER_TABLE_FXML, BasicUserController.class);
        userTablePane = loadPane(userTablePaneLoader, BasicController.class);

        userDetailPaneLoader = loadFXML(USER_DETAIL_FXML, BasicUserController.class);
        userDetailPane = loadPane(userDetailPaneLoader, BasicUserController.class);
    }


}
