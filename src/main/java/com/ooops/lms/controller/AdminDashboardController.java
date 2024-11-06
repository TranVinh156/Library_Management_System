package com.ooops.lms.controller;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminDashboardController extends BasicController {

    @FXML
    private VBox recentIssuelVbox;

    @FXML
    private HBox topBookHbox;

    @FXML
    private Label totalBookLabel;

    @FXML
    private Label totalBorrowLabel;

    @FXML
    private Label totalIssueLabel;

    @FXML
    private Label totalReaderLabel;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    public void initialize() {

        //insertRowToTable(recentIssuelVbox,BasicFuction.ISSUE_RECENT_ROW,20);
        List<Node> rows = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(ISSUE_RECENT_ROW_FXML));
                Node row = loader.load(); // Tải FXML
                rows.add(row); // Thêm nút vào danh sách rows
                recentIssuelVbox.getChildren().add(row); // Thêm vào tableVBox
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        childFitWidthParent(recentIssuelVbox,scrollPane);
        for (int i = 0; i < 11; i++) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(TOPBOOK_CARD_FXML));
                Node row = loader.load(); // Tải FXML
                rows.add(row); // Thêm nút vào danh sách rows
                childFitHeightParent(row,recentIssuelVbox);
                topBookHbox.getChildren().add(row); // Thêm vào tableVBox
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
