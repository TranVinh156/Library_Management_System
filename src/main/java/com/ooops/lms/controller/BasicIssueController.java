package com.ooops.lms.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

public class BasicIssueController extends BasicController {
    private static final String ISSUE_TABLE_FXML = "/com/ooops/lms/library_management_system/AdminIssueTable.fxml";
    private static final String ISSUE_DETAIL_FXML = "/com/ooops/lms/library_management_system/AdminIssueDetail.fxml";
    protected static final String ISSUE_TABLE_ROW_FXML = "/com/ooops/lms/library_management_system/AdminIssueTableRow.fxml";

    protected static final FXMLLoader issueDetailPaneLoader;
    protected static final FXMLLoader issueTablePaneLoader;
    protected static final Node issueDetailPane;
    protected static final Node issueTablePane;


    static {
        issueDetailPaneLoader = loadFXML(ISSUE_DETAIL_FXML,BasicIssueController.class);
        issueDetailPane = loadPane(issueDetailPaneLoader, BasicIssueController.class);
        issueTablePaneLoader = loadFXML(ISSUE_TABLE_FXML,BasicIssueController.class);
        issueTablePane = loadPane(issueTablePaneLoader, BasicIssueController.class);
    }
}
