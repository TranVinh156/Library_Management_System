package com.ooops.lms.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class BasicBookController extends BasicController {
    private static final String BOOK_TABLE_FXML = "/com/ooops/lms/library_management_system/AdminBookTable.fxml";
    private static final String BOOK_DETAIL_FXML = "/com/ooops/lms/library_management_system/AdminBookDetail.fxml";
    protected static final String BOOK_TABLE_ROW_FMXL = "/com/ooops/lms/library_management_system/AdminBookTableRow.fxml";
    protected static final String BOOK_COPY_ROW_FXML = "/com/ooops/lms/library_management_system/AdminBookCopyRow.fxml";

    protected static final FXMLLoader bookTablePaneLoader;
    protected static final Node bookTablePane;
    protected static final FXMLLoader bookDetailPaneLoader;
    protected static final Node bookDetailPane;

    static {
        bookDetailPaneLoader = loadFXML(BOOK_DETAIL_FXML, BasicBookController.class);
        bookDetailPane = loadPane(bookDetailPaneLoader, BasicBookController.class);

        bookTablePaneLoader = loadFXML(BOOK_TABLE_FXML, BasicBookController.class);
        bookTablePane = loadPane(bookTablePaneLoader, BasicBookController.class);
    }
}
