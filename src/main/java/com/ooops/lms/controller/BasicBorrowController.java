package com.ooops.lms.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

public class BasicBorrowController extends BasicController {

    private static final String BORROW_TABLE_FXML = "/com/ooops/lms/library_management_system/AdminBorrowTable.fxml";
    private static final String BORROW_DETAIL_FXML = "/com/ooops/lms/library_management_system/AdminBorrowDetail.fxml";
    protected static final String BORROW_TABLE_ROW_FXML = "/com/ooops/lms/library_management_system/AdminBorrowTableRow.fxml";

    protected static final FXMLLoader borrowTablePaneLoader;
    protected static final FXMLLoader borrowDetailPaneLoader;
    protected static final Node borrowDetailPane;
    protected static final Node borrowTablePane;

    static {
        borrowDetailPaneLoader = loadFXML(BORROW_DETAIL_FXML, BasicController.class);
        borrowDetailPane = loadPane(borrowDetailPaneLoader, BasicController.class);
        borrowTablePaneLoader = loadFXML(BORROW_TABLE_FXML, BasicController.class);
        borrowTablePane = loadPane(borrowTablePaneLoader,BasicController.class);
    }

}
